package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** Request: POST /visitors */
data class VillaVisitorRegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("photo_url") val photoUrl: String? = null,
    @SerializedName("villa_id") val villaId: String,
    @SerializedName("floor_id") val floorId: String,
    @SerializedName("gate_id") val gateId: String,
    @SerializedName("purpose") val purpose: String? = null,
    @SerializedName("vehicle_number") val vehicleNumber: String? = null
)

/** Response: visitor log entry */
data class VillaVisitorLogDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("villa_id") val villaId: String? = null,
    @SerializedName("floor_id") val floorId: String? = null,
    @SerializedName("gate_id") val gateId: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("purpose") val purpose: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)
