package com.app.divine.navigation

import android.content.Context
import android.content.Intent
import com.app.divine.activity.guard.view.GuardMainActivity
import com.app.divine.activity.landing.view.LandingMainActivity
import com.app.core.dagger.preference.AppPreferences

/**
 * Placeholder for push notification deep linking (Phase 5).
 * Call from FCM onNotificationClicked / onDataMessageReceived with payload type and ids.
 * Routes to Resident or Guard graph and can open a specific screen via intent extras.
 */
object DeepLinkRouter {

    const val EXTRA_DEEP_LINK_TYPE = "villa_deep_link_type"
    const val EXTRA_VISITOR_ID = "villa_visitor_id"
    const val EXTRA_VILLA_ID = "villa_villa_id"
    const val EXTRA_FLOOR_ID = "villa_floor_id"
    const val EXTRA_DELIVERY_ID = "villa_delivery_id"
    const val EXTRA_COMPLAINT_ID = "villa_complaint_id"
    const val EXTRA_BILL_ID = "villa_bill_id"
    const val EXTRA_NOTICE_ID = "villa_notice_id"

    /** Notification types from backend (Phase 5 payload). */
    const val TYPE_VISITOR_REQUEST = "VISITOR_REQUEST"
    const val TYPE_VISITOR_STATUS = "VISITOR_STATUS"
    const val TYPE_DELIVERY_ARRIVED = "DELIVERY_ARRIVED"
    const val TYPE_SOS_TRIGGERED = "SOS_TRIGGERED"
    const val TYPE_BILL_GENERATED = "BILL_GENERATED"
    const val TYPE_COMPLAINT_UPDATED = "COMPLAINT_UPDATED"
    const val TYPE_NOTICE_PUBLISHED = "NOTICE_PUBLISHED"

    /**
     * Handle deep link from push notification. Starts the appropriate Activity (Resident or Guard)
     * and attaches extras so the target screen can be shown (e.g. Visitors tab for VISITOR_REQUEST).
     * Implement actual tab/fragment selection in Phase 5 when FCM is wired.
     */
    fun handleNotificationDeepLink(
        context: Context,
        appPreferences: AppPreferences,
        type: String,
        visitorId: String? = null,
        villaId: String? = null,
        floorId: String? = null,
        deliveryId: String? = null,
        complaintId: String? = null,
        billId: String? = null,
        noticeId: String? = null
    ) {
        val intent = if (VillaRoleRouter.isGuard(appPreferences)) {
            Intent(context, GuardMainActivity::class.java)
        } else {
            Intent(context, LandingMainActivity::class.java)
        }.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(EXTRA_DEEP_LINK_TYPE, type)
            visitorId?.let { putExtra(EXTRA_VISITOR_ID, it) }
            villaId?.let { putExtra(EXTRA_VILLA_ID, it) }
            floorId?.let { putExtra(EXTRA_FLOOR_ID, it) }
            deliveryId?.let { putExtra(EXTRA_DELIVERY_ID, it) }
            complaintId?.let { putExtra(EXTRA_COMPLAINT_ID, it) }
            billId?.let { putExtra(EXTRA_BILL_ID, it) }
            noticeId?.let { putExtra(EXTRA_NOTICE_ID, it) }
        }
        context.startActivity(intent)
    }
}
