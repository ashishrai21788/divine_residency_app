package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** Request: POST /guard/attendance/check-in */
data class VillaGuardCheckInRequest(
    @SerializedName("check_in_photo_url") val checkInPhotoUrl: String? = null
)

/** Request: POST /guard/attendance/check-out */
data class VillaGuardCheckOutRequest(
    @SerializedName("check_out_photo_url") val checkOutPhotoUrl: String? = null
)
