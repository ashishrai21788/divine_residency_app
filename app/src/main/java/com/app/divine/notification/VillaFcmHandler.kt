package com.app.divine.notification

import android.content.Context
import android.widget.Toast
import com.app.divine.navigation.DeepLinkRouter
import com.app.divine.realtime.RealtimeDedup
import com.app.core.dagger.preference.AppPreferences
import com.notification.models.NotificationData

/**
 * Handles Villa Society FCM payload: foreground banner + refresh, background route on click.
 * Uses RealtimeDedup to avoid duplicate when same event received via socket and push.
 */
object VillaFcmHandler {

    private const val DATA_TYPE = "type"
    private const val DATA_VISITOR_ID = "visitor_id"
    private const val DATA_VILLA_ID = "villa_id"
    private const val DATA_FLOOR_ID = "floor_id"
    private const val DATA_DELIVERY_ID = "delivery_id"
    private const val DATA_COMPLAINT_ID = "complaint_id"
    private const val DATA_BILL_ID = "billing_id"
    private const val DATA_NOTICE_ID = "notice_id"
    private const val DATA_SOS_ID = "sos_id"

    /** Foreground: show in-app banner unless already handled by socket (dedup). */
    fun onForegroundMessage(context: Context, notificationData: NotificationData) {
        val data = notificationData.data ?: emptyMap()
        val type = DeepLinkRouter.normalizedType(data[DATA_TYPE])
        val visitorId = data[DATA_VISITOR_ID] ?: data["visitor_id"]
        if (RealtimeDedup.wasHandledBySocket(type, visitorId, data["timestamp"])) return
        val title = notificationData.title ?: "Notification"
        val body = notificationData.body ?: ""
        Toast.makeText(context, "$title: $body", Toast.LENGTH_LONG).show()
    }

    /** Background/click: route to Resident or Guard graph with deep link extras (no dedup for click). */
    fun onNotificationClicked(context: Context, appPreferences: AppPreferences, notificationData: NotificationData) {
        val data = notificationData.data ?: emptyMap()
        DeepLinkRouter.handleNotificationDeepLink(
            context = context,
            appPreferences = appPreferences,
            type = data[DATA_TYPE] ?: "",
            visitorId = data[DATA_VISITOR_ID] ?: data["visitor_id"],
            villaId = data[DATA_VILLA_ID] ?: data["villa_id"],
            floorId = data[DATA_FLOOR_ID] ?: data["floor_id"],
            deliveryId = data[DATA_DELIVERY_ID] ?: data["delivery_id"],
            complaintId = data[DATA_COMPLAINT_ID] ?: data["complaint_id"],
            billId = data[DATA_BILL_ID] ?: data["billing_id"],
            noticeId = data[DATA_NOTICE_ID] ?: data["notice_id"],
            sosId = data[DATA_SOS_ID] ?: data["sos_id"],
        )
    }
}
