package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** Request: register FCM token with backend (e.g. POST /notifications/register or /auth/fcm-token) */
data class VillaFcmTokenRequest(
    @SerializedName("fcmToken") val fcmToken: String
)
