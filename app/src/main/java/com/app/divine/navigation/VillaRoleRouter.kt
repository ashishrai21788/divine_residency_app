package com.app.divine.navigation

import android.content.Context
import android.content.Intent
import com.app.divine.activity.guard.view.GuardMainActivity
import com.app.divine.activity.landing.view.LandingMainActivity
import com.app.core.dagger.preference.AppPreferences

/**
 * Role-based routing after Villa Society login.
 * RESIDENT / ADMIN → ResidentGraph (LandingMainActivity).
 * GUARD → GuardGraph (GuardMainActivity).
 */
object VillaRoleRouter {

    private const val ROLE_GUARD = "GUARD"
    private const val ROLE_RESIDENT = "RESIDENT"
    private const val ROLE_ADMIN = "ADMIN"

    fun getPostLoginIntent(context: Context, appPreferences: AppPreferences): Intent {
        val role = appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_USER_ROLE_KEY)
            .uppercase()
        return when (role) {
            ROLE_GUARD -> Intent(context, GuardMainActivity::class.java)
            ROLE_RESIDENT, ROLE_ADMIN -> Intent(context, LandingMainActivity::class.java)
            else -> Intent(context, LandingMainActivity::class.java)
        }.apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP) }
    }

    fun isGuard(appPreferences: AppPreferences): Boolean =
        appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_USER_ROLE_KEY).uppercase() == ROLE_GUARD

    fun isResidentOrAdmin(appPreferences: AppPreferences): Boolean {
        val role = appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_USER_ROLE_KEY).uppercase()
        return role == ROLE_RESIDENT || role == ROLE_ADMIN
    }
}
