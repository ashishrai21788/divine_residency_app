package com.app.divine.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Villa Society notification channels (Phase 5).
 * Create in Application class.
 */
object VillaNotificationChannels {

    const val CHANNEL_VISITOR_ALERTS = "visitor_alerts"
    const val CHANNEL_SOS_ALERTS = "sos_alerts"
    const val CHANNEL_DELIVERY_ALERTS = "delivery_alerts"
    const val CHANNEL_BILLING_ALERTS = "billing_alerts"
    const val CHANNEL_NOTICE_ALERTS = "notice_alerts"

    fun createAll(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = ContextCompat.getSystemService(context, NotificationManager::class.java) ?: return

        val visitorAlerts = NotificationChannel(
            CHANNEL_VISITOR_ALERTS,
            "Visitor alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Visitor at gate and visitor status updates"
            enableLights(true)
            enableVibration(true)
        }
        manager.createNotificationChannel(visitorAlerts)

        val sosAlerts = NotificationChannel(
            CHANNEL_SOS_ALERTS,
            "SOS alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "SOS and emergency alerts"
            enableLights(true)
            enableVibration(true)
            setBypassDnd(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            // Alarm sound can be set via setSound(uri, audioAttributes) with a custom raw resource
        }
        manager.createNotificationChannel(sosAlerts)

        val deliveryAlerts = NotificationChannel(
            CHANNEL_DELIVERY_ALERTS,
            "Delivery alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Delivery arrived at gate"
            enableLights(true)
            enableVibration(true)
        }
        manager.createNotificationChannel(deliveryAlerts)

        val billingAlerts = NotificationChannel(
            CHANNEL_BILLING_ALERTS,
            "Billing alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Bill generated and payment reminders"
            enableLights(true)
            enableVibration(true)
        }
        manager.createNotificationChannel(billingAlerts)

        val noticeAlerts = NotificationChannel(
            CHANNEL_NOTICE_ALERTS,
            "Notice alerts",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Society notices and complaint updates"
            enableLights(true)
            enableVibration(false)
        }
        manager.createNotificationChannel(noticeAlerts)
    }

    /** Map Villa payload type to channel id for system notifications. */
    fun channelIdForType(type: String?): String = when (type) {
        "VISITOR_REQUEST", "VISITOR_STATUS" -> CHANNEL_VISITOR_ALERTS
        "SOS_TRIGGERED" -> CHANNEL_SOS_ALERTS
        "DELIVERY_ARRIVED" -> CHANNEL_DELIVERY_ALERTS
        "BILL_GENERATED" -> CHANNEL_BILLING_ALERTS
        "COMPLAINT_UPDATED", "NOTICE_PUBLISHED" -> CHANNEL_NOTICE_ALERTS
        else -> CHANNEL_VISITOR_ALERTS
    }
}
