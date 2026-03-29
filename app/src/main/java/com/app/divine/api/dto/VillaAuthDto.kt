package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** Request: POST /auth/login */
data class VillaLoginRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("mobile") val mobile: String? = null,
    @SerializedName("password") val password: String
)

/** Request: POST /auth/refresh-token */
data class VillaRefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String
)

/** Request: POST /auth/change-password */
data class VillaChangePasswordRequest(
    @SerializedName("currentPassword") val currentPassword: String,
    @SerializedName("newPassword") val newPassword: String
)

/** Response: login / refresh-token (200/201). Supports legacy `accessToken`+`user` or slim `{ token, role }`. */
data class VillaAuthResponse(
    @SerializedName("accessToken") val accessToken: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("refreshToken") val refreshToken: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("user") val user: VillaUserDto? = null
) {
    fun accessTokenOrToken(): String = (accessToken ?: token).orEmpty()
    fun roleOrFromUser(): String? = role ?: user?.role
}

data class VillaUserDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("mobile") val mobile: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("society_id") val societyId: String? = null,
    @SerializedName("villa_id") val villaId: String? = null,
    @SerializedName("floor_id") val floorId: String? = null,
    @SerializedName("gate_id") val gateId: String? = null
)
