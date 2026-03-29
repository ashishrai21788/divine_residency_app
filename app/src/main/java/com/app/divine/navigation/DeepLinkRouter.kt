package com.app.divine.navigation

import android.content.Context
import android.content.Intent
import com.app.divine.R
import com.app.divine.activity.admin.view.AdminMainActivity
import com.app.divine.activity.guard.view.GuardMainActivity
import com.app.divine.activity.landing.view.LandingMainActivity
import com.app.core.dagger.preference.AppPreferences
import java.util.Locale

/**
 * Push notification deep linking (MyGate-style): map backend FCM [data.type] to the right shell tab.
 * Backend uses lowercase snake_case (e.g. visitor_request, notice).
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
    const val EXTRA_SOS_ID = "villa_sos_id"

    /** Legacy uppercase aliases → normalized snake_case. */
    private val LEGACY_TYPE_ALIASES = mapOf(
        "VISITOR_REQUEST" to "visitor_request",
        "VISITOR_STATUS" to "visitor_approval",
        "DELIVERY_ARRIVED" to "delivery_arrived",
        "SOS_TRIGGERED" to "sos_triggered",
        "BILL_GENERATED" to "bill_generated",
        "COMPLAINT_UPDATED" to "complaint_resolved",
        "NOTICE_PUBLISHED" to "notice",
    )

    fun normalizedType(raw: String?): String {
        val t = raw?.trim().orEmpty()
        if (t.isEmpty()) return ""
        LEGACY_TYPE_ALIASES[t.uppercase(Locale.US)]?.let { return it }
        return t.lowercase(Locale.US).replace('-', '_')
    }

    /** Bottom nav item for resident shell ([R.menu.bottom_nav_menu_resident]). */
    fun residentTabForNotificationType(type: String): Int? = when (normalizedType(type)) {
        "visitor_request", "visitor_approval", "visitor_rejected" -> R.id.menu_resident_visitors
        "delivery_arrived" -> R.id.menu_resident_dashboard
        "bill_generated", "payment_received", "due_reminder", "late_fee_warning" -> R.id.menu_resident_billing
        "complaint_created", "complaint_assigned", "complaint_resolved" -> R.id.menu_resident_complaints
        "notice" -> R.id.menu_resident_dashboard
        "sos_triggered", "blacklist_attempt" -> R.id.menu_resident_dashboard
        else -> null
    }

    fun guardTabForNotificationType(type: String): Int? = when (normalizedType(type)) {
        "visitor_request", "visitor_approval", "visitor_rejected" -> R.id.menu_guard_visitors
        "delivery_arrived" -> R.id.menu_guard_deliveries
        "sos_triggered" -> R.id.menu_guard_sos
        "blacklist_attempt" -> R.id.menu_guard_dashboard
        else -> null
    }

    fun adminTabForNotificationType(type: String): Int? = when (normalizedType(type)) {
        "complaint_created", "complaint_assigned", "complaint_resolved" -> R.id.menu_admin_complaints
        "notice" -> R.id.menu_admin_notices
        "visitor_request", "visitor_approval", "visitor_rejected" -> R.id.menu_admin_logs
        "sos_triggered", "blacklist_attempt", "bill_generated", "payment_received", "due_reminder", "late_fee_warning" ->
            R.id.menu_admin_overview
        else -> null
    }

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
        noticeId: String? = null,
        sosId: String? = null,
    ) {
        val intent = when {
            VillaRoleRouter.isGuard(appPreferences) -> Intent(context, GuardMainActivity::class.java)
            VillaRoleRouter.isAdmin(appPreferences) -> Intent(context, AdminMainActivity::class.java)
            else -> Intent(context, LandingMainActivity::class.java)
        }.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(EXTRA_DEEP_LINK_TYPE, normalizedType(type).ifEmpty { type })
            visitorId?.let { putExtra(EXTRA_VISITOR_ID, it) }
            villaId?.let { putExtra(EXTRA_VILLA_ID, it) }
            floorId?.let { putExtra(EXTRA_FLOOR_ID, it) }
            deliveryId?.let { putExtra(EXTRA_DELIVERY_ID, it) }
            complaintId?.let { putExtra(EXTRA_COMPLAINT_ID, it) }
            billId?.let { putExtra(EXTRA_BILL_ID, it) }
            noticeId?.let { putExtra(EXTRA_NOTICE_ID, it) }
            sosId?.let { putExtra(EXTRA_SOS_ID, it) }
        }
        context.startActivity(intent)
    }
}
