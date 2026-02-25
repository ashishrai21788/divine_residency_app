package com.notification.models

import android.os.Parcelable
import com.google.firebase.messaging.RemoteMessage
import kotlinx.parcelize.Parcelize

/**
 * Data model for Firebase notification data
 */
@Parcelize
data class NotificationData(
    val title: String? = null,
    val body: String? = null,
    val imageUrl: String? = null,
    val data: Map<String, String> = emptyMap(),
    val notificationId: Int = 0,
    val channelId: String? = null,
    val priority: Int = 0,
    val sound: String? = null,
    val clickAction: String? = null,
    val tag: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable {
    companion object {
        fun fromRemoteMessage(message: RemoteMessage): NotificationData {
            val notification = message.notification
            return NotificationData(
                title = notification?.title,
                body = notification?.body,
                imageUrl = notification?.imageUrl?.toString(),
                data = message.data,
                notificationId = message.messageId?.hashCode() ?: 0,
                channelId = message.data["channel_id"],
                priority = notification?.notificationPriority ?: 0,
                sound = notification?.sound?.toString(),
                clickAction = notification?.clickAction,
                tag = message.data["tag"],
                color = message.data["color"],
                icon = message.data["icon"]
            )
        }
    }
} 