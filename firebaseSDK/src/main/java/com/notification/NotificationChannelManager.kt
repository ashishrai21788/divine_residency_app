package com.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build

/**
 * Manages notification channels and preferences for Firebase Cloud Messaging
 */
class NotificationChannelManager private constructor(private val context: Context) {

    companion object {
        private const val TAG = "NotificationChannelManager"
        private const val PREFS_NAME = "fcm_preferences"
        private const val KEY_FCM_TOKEN = "fcm_token"
        private const val DEFAULT_CHANNEL_ID = "default_channel"
        private const val DEFAULT_CHANNEL_NAME = "Default Channel"
        private const val DEFAULT_CHANNEL_DESCRIPTION = "Default notification channel"

        @Volatile
        private var instance: NotificationChannelManager? = null

        fun getInstance(context: Context): NotificationChannelManager {
            return instance ?: synchronized(this) {
                instance ?: NotificationChannelManager(context.applicationContext).also { instance = it }
            }
        }
    }

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Creates the default notification channel for Android O and above
     */
    fun createDefaultChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                DEFAULT_CHANNEL_ID,
                DEFAULT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = DEFAULT_CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Creates a custom notification channel
     */
    fun createChannel(
        channelId: String,
        channelName: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        description: String? = null,
        enableLights: Boolean = true,
        enableVibration: Boolean = true
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
                this.enableLights(enableLights)
                this.enableVibration(enableVibration)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Saves the FCM token to SharedPreferences
     */
    fun saveFcmToken(token: String) {
        preferences.edit().putString(KEY_FCM_TOKEN, token).apply()
    }

    /**
     * Retrieves the saved FCM token
     */
    fun getSavedFcmToken(): String? {
        return preferences.getString(KEY_FCM_TOKEN, null)
    }

    /**
     * Clears the saved FCM token
     */
    fun clearFcmToken() {
        preferences.edit().remove(KEY_FCM_TOKEN).apply()
    }

    /**
     * Gets the default notification channel ID
     */
    fun getDefaultChannelId(): String = DEFAULT_CHANNEL_ID

    /**
     * Checks if a notification channel exists
     */
    fun channelExists(channelId: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.getNotificationChannel(channelId) != null
        } else {
            true
        }
    }

    /**
     * Deletes a notification channel
     */
    fun deleteChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(channelId)
        }
    }
} 