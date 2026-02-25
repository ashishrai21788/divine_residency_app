package com.app.divine

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.app.core.dagger.component.CoreComponent
import com.app.core.dagger.component.DaggerCoreComponent
import com.app.core.dagger.module.ContextModule
import com.app.core.dagger.module.NetworkModule
import com.app.core.utils.CoreComponentProvider
import com.app.divine.utils.LanguageManager
import com.app.divine.utils.SimpleLifecycleLogger
import com.notification.FirebaseNotificationManager
import com.notification.models.NotificationData


class AppApplication : Application(), CoreComponentProvider {
    lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        // Initialize Dagger with temporary implementation
        coreComponent =
            DaggerCoreComponent.builder().contextModule(ContextModule(this)).networkModule(
                NetworkModule()
            ).build()

        // Initialize LanguageManager
        LanguageManager.initialize(this)

        // Initialize SimpleLifecycleLogger
        SimpleLifecycleLogger.getInstance().initialize(this)

        // Initialize FirebaseNotificationManager here
        FirebaseNotificationManager.getInstance().initialize(applicationContext, object : FirebaseNotificationManager.NotificationCallback {
            override fun onMessageReceived(notificationData: NotificationData) {
                Log.d("FCM_Callback", "onMessageReceived: ${notificationData.title}")
                // Handle foreground notification
            }

            override fun onBackgroundMessageReceived(notificationData: NotificationData) {
                Log.d("FCM_Callback", "onBackgroundMessageReceived: ${notificationData.title}")
                // Handle background data messages or when showSystemNotification is false
            }

            override fun onDataMessageReceived(notificationData: NotificationData) {
                Log.d("FCM_Callback", "onDataMessageReceived: ${notificationData.data}")
                // Handle data messages
            }

            override fun onNewToken(token: String) {
                Log.d("FCM_Callback", "onNewToken: $token")
                // Handle new token, e.g., send to backend
                sendPushToken("", token) // Use the existing method to send token to backend
            }

            override fun onNotificationAction(action: String, data: Map<String, String>) {
                Log.d("FCM_Callback", "onNotificationAction: $action with data $data")
                // Handle notification action clicks
            }

            override fun onNotificationClicked(notificationData: NotificationData) {
                Log.d("FCM_Callback", "onNotificationClicked: ${notificationData.title}")
                // Handle notification body clicks, potentially navigate the user
            }

            override fun onTokenReceived(token: String) {
                Log.d("FCM_Callback", "onNotificationClicked: ${token}")
            }

            override fun onError(error: String) {
                Log.e("FCM_Callback", "onError: $error")
                // Handle errors from the SDK
            }
        })

    }

    private fun sendPushToken(string: String, token: String) {

    }

    override fun provideCoreComponent(): CoreComponent {
       return coreComponent
    }

    override fun forceLoggedOut() {
        TODO("Not yet implemented")
    }

    override fun logFacebookEvent(name: String?) {
        TODO("Not yet implemented")
    }

    override fun removeChatSupport() {
        TODO("Not yet implemented")
    }

    override fun getChatSupportVisibilityData(): LiveData<Boolean> {
        TODO("Not yet implemented")
    }

    override fun logFirebaseEvent(
        pageIdentifier: String,
        eventName: String,
        eventParams: HashMap<String, String>,
        keepActualKey: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun fetchFCMToken(
        firebaseSenderId: String?,
        callBack: ((String) -> Unit)?
    ) {
        TODO("Not yet implemented")
    }
}