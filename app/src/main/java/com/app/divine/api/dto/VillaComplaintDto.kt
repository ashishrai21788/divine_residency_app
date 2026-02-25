package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** Request: POST /complaints */
data class VillaComplaintCreateRequest(
    @SerializedName("villa_id") val villaId: String,
    @SerializedName("floor_id") val floorId: String,
    @SerializedName("category") val category: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("photo_url") val photoUrl: String? = null
)

data class VillaComplaintDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("villa_id") val villaId: String? = null,
    @SerializedName("floor_id") val floorId: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)
