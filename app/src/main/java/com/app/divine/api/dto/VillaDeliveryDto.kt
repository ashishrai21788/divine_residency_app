package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** Request: POST /deliveries */
data class VillaDeliveryCreateRequest(
    @SerializedName("description") val description: String,
    @SerializedName("villa_id") val villaId: String,
    @SerializedName("floor_id") val floorId: String,
    @SerializedName("gate_id") val gateId: String
)

data class VillaDeliveryDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("villa_id") val villaId: String? = null,
    @SerializedName("floor_id") val floorId: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)
