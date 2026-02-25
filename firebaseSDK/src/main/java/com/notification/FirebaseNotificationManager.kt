package com.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.notification.models.NotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

/**
 * A production-grade Firebase Cloud Messaging (FCM) notification manager for Android applications.
 * This class handles FCM token management, topic subscriptions, and provides callbacks for various operations.
 */
class FirebaseNotificationManager private constructor() {

    companion object {
        private const val TAG = "FirebaseNotificationManager"
        private const val ACTION_NOTIFICATION_CLICK = "come.notification.ACTION_NOTIFICATION_CLICK"
        private const val EXTRA_NOTIFICATION_DATA = "notification_data"
        private const val NOTIFICATION_COOLDOWN_MS = 30000L // 30 seconds cooldown
        private const val MAX_IMAGE_SIZE = 1024 // Maximum image size in pixels
        
        @Volatile
        private var instance: FirebaseNotificationManager? = null

        fun getInstance(): FirebaseNotificationManager {
            return instance ?: synchronized(this) {
                instance ?: FirebaseNotificationManager().also { instance = it }
            }
        }

    }

    // Changed access modifiers to internal for accessibility from CustomFirebaseMessagingService
    var tokenCallback: TokenCallback? = null
    internal var notificationCallback: NotificationCallback? = null
    private lateinit var channelManager: NotificationChannelManager
    private val topicSubscriptions = ConcurrentHashMap<String, Boolean>()
    private val _tokenState = MutableStateFlow<String?>(null)
    private var isAppInForeground = false
    private var showSystemNotification = true
    private val gson = Gson()
    
    // Track recent notifications to prevent duplicates
    private val recentNotifications = ConcurrentHashMap<String, Long>()
    private val notificationManager: NotificationManager? = null // This property seems unused, consider removing

    /**
     * Initialize Firebase from google-services.json located in the app module.
     * This method should be called once from your application's entry point (e.g., Application class).
     * @param context Application context
     * @param callback Callback interface for token updates and notification events
     * @throws FileNotFoundException if google-services.json is not found
     * @throws IllegalArgumentException if Firebase configuration is invalid
     */
    fun initialize(context: Context, callback: NotificationCallback) {
        try {
//            // Find and read google-services.json from the app module
//            val googleServicesJsonStream = findGoogleServicesJson(context)
//            // Use .use to ensure the stream is closed automatically
//            val googleServicesJson = googleServicesJsonStream.bufferedReader().use { it.readText() }
//
//            // Parse and extract Firebase options
//            val firebaseOptions = parseGoogleServicesJson(context, googleServicesJson)

            val firebaseOptions = FirebaseOptions.fromResource(context)
            // Initialize FirebaseApp if not already initialized
            if (FirebaseApp.getApps(context).isEmpty()) {
                firebaseOptions?.let { FirebaseApp.initializeApp(context, it) }
                Log.d(TAG, "FirebaseApp initialized successfully from google-services.json")
            } else {
                Log.d(TAG, "FirebaseApp already initialized.")
                // Optional: Verify if the existing initialization matches the google-services.json
                // This can be complex and might not be necessary depending on your use case.
            }

            // Initialize channel manager and set callbacks
            channelManager = NotificationChannelManager.getInstance(context)
            channelManager.createDefaultChannel()
            this.notificationCallback = callback // Set the notification callback
            
            // Create a TokenCallback that delegates to the provided NotificationCallback
            this.tokenCallback = object : TokenCallback {
                override fun onTokenReceived(token: String) {
                    callback.onNewToken(token)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }

            // Get and observe FCM token after FirebaseApp is initialized
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                Log.d(TAG, "FCM Token: $token")
                _tokenState.value = token
                tokenCallback?.onTokenReceived(token) // Use the new tokenCallback
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to get FCM token after initialization", e)
                tokenCallback?.onError("Failed to get FCM token: ${e.message}") // Use the new tokenCallback
            }

        } catch (e: FileNotFoundException) {
            val errorMessage = "Failed to initialize Firebase: google-services.json not found. Ensure it's in the app module's res/raw directory or assets folder."
            Log.e(TAG, errorMessage, e)
            callback.onError(errorMessage)
        } catch (e: Exception) {
            val errorMessage = "Failed to initialize Firebase: ${e.message}"
            Log.e(TAG, errorMessage, e)
            callback.onError(errorMessage)
        }
    }

    /**
     * Set whether to show system notifications
     * @param show true to show system notifications, false to handle them in the app
     */
    fun setShowSystemNotification(show: Boolean) {
        this.showSystemNotification = show
    }

    /**
     * Handle notification intent
     * @param context Application context
     * @param intent Intent containing notification data
     */
    fun handleIntent(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_NOTIFICATION_CLICK -> {
                val notificationDataJson = intent.getStringExtra(EXTRA_NOTIFICATION_DATA)
                if (notificationDataJson != null) {
                    try {
                        val notificationData = gson.fromJson(notificationDataJson, NotificationData::class.java)
                        notificationCallback?.onNotificationClicked(notificationData)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing notification data from intent", e)
                    }
                }
            }
        }
    }

    /**
     * Update app foreground state
     * @param isInForeground Whether the app is in foreground
     */
    fun updateAppState(isInForeground: Boolean) {
        this.isAppInForeground = isInForeground
        Log.d(TAG, "App state updated: isInForeground = $isInForeground")
    }

    /**
     * Process incoming message
     * @param context Application context
     * @param message RemoteMessage received from FCM
     * @param isBackground Whether the message was received in background
     */
    fun processMessage(context: Context, message: RemoteMessage, isBackground: Boolean) {
        try {
            Log.d(TAG, "Processing message: ${message.messageId}")
            val notificationData = NotificationData.fromRemoteMessage(message)
            
            // Check for duplicate notification
            if (isDuplicateNotification(notificationData)) {
                Log.d(TAG, "Duplicate notification detected, skipping: ${message.messageId}")
                return
            }

            // Store notification for deduplication
            storeNotification(notificationData)
            
            when {
                notificationData.title != null || notificationData.body != null -> {
                    if (isAppInForeground) {
                        // Handle foreground notification
                        Log.d(TAG, "Handling foreground notification")
                        notificationCallback?.onMessageReceived(notificationData)
                    } else if (showSystemNotification) {
                        // Show system notification for background
                        Log.d(TAG, "Showing system notification")
                        // This should call the showSystemNotification method, 
                        // not directly the callback as it's for showing the UI.
                        showSystemNotification(context, notificationData)
                         notificationCallback?.onMessageReceived(notificationData) // Removed redundant callback call
                    } else {
                        // Let the app handle the notification
                        Log.d(TAG, "Handling background notification")
                        notificationCallback?.onBackgroundMessageReceived(notificationData)
                    }
                }
                notificationData.data.isNotEmpty() -> {
                    // Handle data message
                    Log.d(TAG, "Handling data message")
                    notificationCallback?.onDataMessageReceived(notificationData)
                }
            }

            // Handle notification actions (assuming actions are handled via data map or notification click)
            // The previous custom view action handling is removed. Actions will need to be processed 
            // either in the onNotificationClicked callback or by parsing the intent data in the activity.
            // notificationData.data["action"]?.let { action ->
            //     handleNotificationAction(action, notificationData.data) // This method is also removed
            // }

        } catch (e: Exception) {
            Log.e(TAG, "Error processing message", e)
            notificationCallback?.onError("Error processing message: ${e.message}")
        }
    }

    /**
     * Check if a notification is a duplicate
     * @param notificationData The notification data to check
     * @return true if the notification is a duplicate
     */
    private fun isDuplicateNotification(notificationData: NotificationData): Boolean {
        val notificationKey = generateNotificationKey(notificationData)
        val lastShownTime = recentNotifications[notificationKey]
        
        if (lastShownTime != null) {
            val timeSinceLastShown = System.currentTimeMillis() - lastShownTime
            if (timeSinceLastShown < NOTIFICATION_COOLDOWN_MS) {
                Log.d(TAG, "Notification is within cooldown period: $timeSinceLastShown ms")
                return true
            }
        }
        
        return false
    }

    /**
     * Store notification for deduplication
     * @param notificationData The notification data to store
     */
    private fun storeNotification(notificationData: NotificationData) {
        val notificationKey = generateNotificationKey(notificationData)
        recentNotifications[notificationKey] = System.currentTimeMillis()
        
        // Clean up old notifications
        cleanupOldNotifications()
    }

    /**
     * Generate a unique key for a notification
     * @param notificationData The notification data
     * @return A unique key for the notification
     */
    private fun generateNotificationKey(notificationData: NotificationData): String {
        return "${notificationData.title}_${notificationData.body}_${notificationData.data.hashCode()}"
    }

    /**
     * Clean up old notifications from the tracking map
     */
    private fun cleanupOldNotifications() {
        val currentTime = System.currentTimeMillis()
        recentNotifications.entries.removeIf { (_, timestamp) ->
            currentTime - timestamp > NOTIFICATION_COOLDOWN_MS
        }
    }

    /**
     * Show system notification
     */
    @SuppressLint("RemoteViewLayout") // Keep annotation, might be needed for other uses or remove if no longer relevant
    private fun showSystemNotification(context: Context, notificationData: NotificationData) {
        try {
            if (!showSystemNotification) return

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = channelManager.getDefaultChannelId()

            // Cancel any existing notification with the same ID
            notificationManager.cancel(notificationData.notificationId)

            // Create base intent for notification click
            val intent = Intent(context, context.javaClass).apply {
                action = ACTION_NOTIFICATION_CLICK
                // Assuming NotificationData is Parcelable or Serializable to pass it via Intent
                putExtra(EXTRA_NOTIFICATION_DATA, gson.toJson(notificationData)) // Pass as JSON string
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            // Handle deep link if present (restoring previous deep link handling)
            notificationData.data["deep_link"]?.let { deepLink ->
                try {
                    intent.data = Uri.parse(deepLink)
                } catch (e: Exception) {
                    Log.e(TAG, "Invalid deep link: $deepLink", e)
                }
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                notificationData.notificationId,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )

            // Create notification builder with basic content
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Using a default small icon
                .setContentTitle(notificationData.title)
                .setContentText(notificationData.body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(notificationData.priority)
                .setGroup(notificationData.tag)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)

            // Handle notification style based on content (Restoring previous logic)
            when (
                // Handle image notification
                notificationData.imageUrl != null
            ) {
                true -> {
                    handleImageNotification(context, notificationBuilder, notificationData)
                }
                false -> {
                    when {
                        // Handle text with links (restoring previous logic)
                        notificationData.data["links"] != null -> {
                            handleTextWithLinksNotification(context, notificationBuilder, notificationData)
                        }
                        // Handle expandable text (restoring previous logic)
                        notificationData.data["expandable_text"] != null -> {
                            handleExpandableTextNotification(context, notificationBuilder, notificationData)
                        }
                         // Handle messaging style (restoring previous logic)
                        notificationData.data["messaging_style"] != null -> {
                             handleMessagingStyleNotification(context, notificationBuilder, notificationData)
                        }
                        // Default style (restoring previous logic)
                        else -> {
                            handleDefaultStyleNotification(context, notificationBuilder, notificationData)
                        }
                    }
                }
            }

             // Set notification color if provided
             notificationData.color?.let { color ->
                 try {
                     notificationBuilder.setColor(android.graphics.Color.parseColor(color))
                 } catch (e: Exception) {
                     Log.e(TAG, "Invalid color format: $color", e)
                 }catch (e: IllegalArgumentException) { // Catch specific color parsing errors
                      Log.e(TAG, "Invalid color format: $color", e)
                 }
             }

            // Show the notification
            notificationManager.notify(notificationData.notificationId, notificationBuilder.build())
            Log.d(TAG, "System notification shown successfully with standard style")
        } catch (e: Exception) {
            Log.e(TAG, "Error showing system notification with standard style", e)
        }
    }

    /**
     * Handle image notification using BigPictureStyle
     */
    private fun handleImageNotification(
        context: Context,
        builder: NotificationCompat.Builder,
        notificationData: NotificationData
    ) {
        try {
            val imageUrl = notificationData.imageUrl
            if (imageUrl != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val bitmap = loadImageFromUrl(imageUrl)
                        if (bitmap != null) {
                            val style = NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap)
                                .setBigContentTitle(notificationData.title)
                                .setSummaryText(notificationData.body)

                            with(Dispatchers.Main) {
                                builder.setStyle(style)
                                    .setLargeIcon(bitmap) // Set large icon as the image
                                    .setContentTitle(notificationData.title)
                                    .setContentText(notificationData.body)
                                
                                // Re-notify to update the notification with the image
                                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.notify(notificationData.notificationId, builder.build())
                            }
                        } else {
                            Log.e(TAG, "Failed to load image from URL for BigPictureStyle: $imageUrl")
                            // Fallback to default style if image loading fails
                             handleDefaultStyleNotification(context, builder, notificationData)
                             // Re-notify with default style
                            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.notify(notificationData.notificationId, builder.build())
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing image for BigPictureStyle notification", e)
                         // Fallback to default style on error
                         handleDefaultStyleNotification(context, builder, notificationData)
                         // Re-notify with default style
                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.notify(notificationData.notificationId, builder.build())
                    }
                }
            } else {
                 // Fallback to default style if imageUrl is null
                 handleDefaultStyleNotification(context, builder, notificationData)
                  // Re-notify with default style
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(notificationData.notificationId, builder.build())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling image notification (BigPictureStyle)", e)
             // Fallback to default style on error
             handleDefaultStyleNotification(context, builder, notificationData)
              // Re-notify with default style
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationData.notificationId, builder.build())
        }
    }

     /**
      * Handle text with links notification using BigTextStyle and adding actions
      */
     private fun handleTextWithLinksNotification(
         context: Context,
         builder: NotificationCompat.Builder,
         notificationData: NotificationData
     ) {
         try {
             val links = notificationData.data["links"]?.split(",")
             if (!links.isNullOrEmpty()) {
                 val style = NotificationCompat.BigTextStyle()
                     .bigText(notificationData.body)
                 builder.setStyle(style)

                 // Add action buttons for links (restoring previous logic)
                 links.forEachIndexed { index, link ->
                     try {
                         val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                         val linkPendingIntent = PendingIntent.getActivity(
                             context,
                             notificationData.notificationId + index, // Unique request code
                             linkIntent,
                             PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                         )
                         builder.addAction(0, "Link ${index + 1}", linkPendingIntent)
                     } catch (e: Exception) {
                          Log.e(TAG, "Error creating link action for $link", e)
                     }
                 }
             } else {
                  // Fallback to default style if no links found
                  handleDefaultStyleNotification(context, builder, notificationData)
             }
         } catch (e: Exception) {
             Log.e(TAG, "Error handling text with links notification", e)
              // Fallback to default style on error
              handleDefaultStyleNotification(context, builder, notificationData)
         }
     }

     /**
      * Handle expandable text notification using BigTextStyle
      */
     private fun handleExpandableTextNotification(
         context: Context,
         builder: NotificationCompat.Builder,
         notificationData: NotificationData
     ) {
         try {
             val expandableText = notificationData.data["expandable_text"]
             if (expandableText != null) {
                 val style = NotificationCompat.BigTextStyle()
                     .bigText(expandableText)
                     .setBigContentTitle(notificationData.title)
                 builder.setStyle(style)
             } else {
                  // Fallback to default style if no expandable text found
                  handleDefaultStyleNotification(context, builder, notificationData)
             }
         } catch (e: Exception) {
             Log.e(TAG, "Error handling expandable text notification", e)
              // Fallback to default style on error
              handleDefaultStyleNotification(context, builder, notificationData)
         }
     }

     /**
      * Handle messaging style notification
      */
     private fun handleMessagingStyleNotification(
         context: Context,
         builder: NotificationCompat.Builder,
         notificationData: NotificationData
     ) {
         try {
             val messagingStyle = NotificationCompat.MessagingStyle(Person.Builder().setName("Me").build())
             val messages = notificationData.data["messages"]?.split("|")
             if (!messages.isNullOrEmpty()) {
                 messages.forEach { message ->
                     val parts = message.split(":")
                     if (parts.size >= 2) {
                         val sender = parts[0]
                         val text = parts[1]
                         val timestamp = parts.getOrNull(2)?.toLongOrNull() ?: System.currentTimeMillis()
                         messagingStyle.addMessage(text, timestamp, Person.Builder().setName(sender).build())
                     }
                 }
                 builder.setStyle(messagingStyle)
             } else {
                 // Fallback to default style if no messages found
                 handleDefaultStyleNotification(context, builder, notificationData)
             }
         } catch (e: Exception) {
             Log.e(TAG, "Error handling messaging style notification", e)
              // Fallback to default style on error
              handleDefaultStyleNotification(context, builder, notificationData)
         }
     }

     /**
      * Handle default style notification using BigTextStyle or standard style
      */
     private fun handleDefaultStyleNotification(
         context: Context,
         builder: NotificationCompat.Builder,
         notificationData: NotificationData
     ) {
         // Use BigTextStyle if body is long, otherwise standard style
         if (!notificationData.body.isNullOrEmpty() && notificationData.body.length > 50) { // Arbitrary length check
              builder.setStyle(NotificationCompat.BigTextStyle().bigText(notificationData.body))
         } else {
              // Standard notification style (title and body)
              // ContentTitle and ContentText are already set in the main builder
         }
     }

    /**
     * Load image from URL
     * Note: This method is currently not used for displaying images in the custom RemoteViews notification.
     * Dynamic image loading into RemoteViews is complex and typically requires updating the notification after loading.
     */
    private fun loadImageFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.connect()
            
            val inputStream = connection.getInputStream()
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeStream(inputStream, null, this)
                inSampleSize = calculateInSampleSize(this, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
                inJustDecodeBounds = false
            }
            
            // Create a new input stream since the previous one was consumed
            // Ensure the connection is properly closed after getting the stream
            val newInputStream = url.openStream()
            val bitmap = BitmapFactory.decodeStream(newInputStream, null, options)
            newInputStream.close() // Close the stream
            
            if (bitmap == null) {
                Log.e(TAG, "Failed to decode bitmap from URL: $imageUrl")
                null
            } else {
                bitmap
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error loading image from URL: $imageUrl", e)
            null
        } catch (e: Exception) {
             Log.e(TAG, "An unexpected error occurred while loading image from URL: $imageUrl", e)
             null
        }
    }

    /**
     * Calculate sample size for image scaling
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * Set up notification callback
     * @param callback Callback interface for notification events
     */
    fun setNotificationCallback(callback: NotificationCallback) {
        this.notificationCallback = callback
    }

    /**
     * Get the current FCM token
     * @param callback Callback to receive the token
     */
    fun getToken(callback: TokenCallback) {
        // FirebaseMessaging.getInstance() implicitly requires FirebaseApp to be initialized.
        // The initialize method should be called before getToken.
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                // channelManager.saveFcmToken(token) // This method doesn't exist in the current code, consider removing or implementing
                _tokenState.value = token
                callback.onTokenReceived(token)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get FCM token", e)
                callback.onError("Failed to get FCM token: ${e.message}")
            }
        }
    }

    /**
     * Subscribe to a topic
     * @param topic Topic name to subscribe to
     * @param callback Callback for subscription status
     */
    fun subscribeToTopic(topic: String, callback: TopicCallback) {
         // FirebaseMessaging.getInstance() implicitly requires FirebaseApp to be initialized.
         // The initialize method should be called before subscribing.
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    topicSubscriptions[topic] = true
                    callback.onTopicOperationSuccess(topic, true)
                } else {
                    Log.e(TAG, "Failed to subscribe to topic: $topic", task.exception)
                    callback.onTopicOperationError(topic, task.exception?.message ?: "Unknown error")
                }
            }
    }

    /**
     * Unsubscribe from a topic
     * @param topic Topic name to unsubscribe from
     * @param callback Callback for unsubscription status
     */
    fun unsubscribeFromTopic(topic: String, callback: TopicCallback) {
         // FirebaseMessaging.getInstance() implicitly requires FirebaseApp to be initialized.
         // The initialize method should be called before unsubscribing.
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    topicSubscriptions[topic] = false
                    callback.onTopicOperationSuccess(topic, false)
                } else {
                    Log.e(TAG, "Failed to unsubscribe from topic: $topic", task.exception)
                    callback.onTopicOperationError(topic, task.exception?.message ?: "Unknown error")
                }
            }
    }

    /**
     * Check if subscribed to a topic
     * @param topic Topic name to check
     * @return Boolean indicating subscription status
     */
    fun isSubscribedToTopic(topic: String): Boolean {
        return topicSubscriptions[topic] ?: false
    }

    /**
     * Handle notification action
     * Note: This method is no longer used after removing the custom action handling logic.
     */
    // private fun handleNotificationAction(action: String, data: Map<String, String>) {
    //     Log.d(TAG, "Handling notification action: $action")
    //     notificationCallback?.onNotificationAction(action, data)
    // }

    /**
     * Interface for token-related callbacks
     */
    interface TokenCallback {
        fun onTokenReceived(token: String)
        fun onError(error: String)
    }

    /**
     * Interface for topic subscription/unsubscription callbacks
     */
    interface TopicCallback {
        fun onTopicOperationSuccess(topic: String, isSubscribed: Boolean)
        fun onTopicOperationError(topic: String, error: String)
    }

    /**
     * Interface for notification callbacks
     */
    interface NotificationCallback : TokenCallback { // NotificationCallback should extend TokenCallback
        fun onMessageReceived(notificationData: NotificationData)
        fun onBackgroundMessageReceived(notificationData: NotificationData)
        fun onDataMessageReceived(notificationData: NotificationData)
        fun onNewToken(token: String)
        fun onNotificationAction(action: String, data: Map<String, String>) // This might need adjustment if action handling changes
        fun onNotificationClicked(notificationData: NotificationData)
        // Removed onError as it's now in TokenCallback
        // fun onError(error: String)
    }
}

/**
 * Extension of FirebaseMessagingService to handle incoming messages
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("CustomFirebaseMessagingService", "Message received: ${message.messageId}")
        // Ensure the application context is used when calling processMessage
        applicationContext?.let { context ->
             FirebaseNotificationManager.getInstance().processMessage(context, message, false)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("CustomFirebaseMessagingService", "New token: $token")
        // Pass the new token to the main notificationCallback
        FirebaseNotificationManager.getInstance().notificationCallback?.onNewToken(token)
        // Also pass to the tokenCallback for consistency, though notificationCallback extends TokenCallback
        FirebaseNotificationManager.getInstance().tokenCallback?.onTokenReceived(token)
    }


}
