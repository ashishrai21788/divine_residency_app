package com.app.divine.session

import com.app.core.dagger.preference.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Single source of truth for session after login. Mirrors [AppPreferences] Villa keys.
 */
class SessionRepository(private val prefs: AppPreferences) {

    private val _session = MutableStateFlow<UserSession?>(null)
    val session: StateFlow<UserSession?> = _session.asStateFlow()

    fun refreshFromPreferences() {
        if (!prefs.getLogin()) {
            _session.value = null
            return
        }
        val role = UserRole.fromApiValue(prefs.getAllPreferenceTypeString(AppPreferences.VILLA_USER_ROLE_KEY))
        val userId = prefs.getAllPreferenceTypeString(AppPreferences.VILLA_USER_ID_KEY)
        val societyId = prefs.getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        if (userId.isEmpty() && societyId.isEmpty() && role == UserRole.UNKNOWN) {
            _session.value = null
            return
        }
        _session.value = UserSession(
            userId = userId,
            role = role,
            societyId = societyId,
            name = prefs.getAllPreferenceTypeString(AppPreferences.VILLA_USER_NAME_KEY).takeIf { it.isNotBlank() },
            villaId = prefs.getAllPreferenceTypeString(AppPreferences.VILLA_VILLA_ID_KEY).takeIf { it.isNotBlank() },
            floorId = prefs.getAllPreferenceTypeString(AppPreferences.VILLA_FLOOR_ID_KEY).takeIf { it.isNotBlank() },
            gateId = prefs.getAllPreferenceTypeString(AppPreferences.VILLA_GATE_ID_KEY).takeIf { it.isNotBlank() },
        )
    }

    fun clear() {
        _session.value = null
    }
}
