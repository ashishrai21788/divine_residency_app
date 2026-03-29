package com.app.divine.session

import com.app.core.dagger.preference.AppPreferences

enum class UserRole {
    RESIDENT,
    GUARD,
    ADMIN,
    UNKNOWN;

    companion object {
        fun fromApiValue(raw: String?): UserRole {
            if (raw.isNullOrBlank()) return UNKNOWN
            return when (raw.uppercase()) {
                "RESIDENT" -> RESIDENT
                "GUARD" -> GUARD
                "ADMIN" -> ADMIN
                else -> UNKNOWN
            }
        }
    }
}

/**
 * Read model for the logged-in Villa user (backed by [AppPreferences]).
 */
data class UserSession(
    val userId: String,
    val role: UserRole,
    val societyId: String,
    val name: String?,
    val villaId: String?,
    val floorId: String?,
    val gateId: String?,
)
