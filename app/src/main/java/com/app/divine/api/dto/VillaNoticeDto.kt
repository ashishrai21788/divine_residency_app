package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

data class VillaNoticeDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("_id") val idAlt: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null
) {
    fun stableId(): String = id ?: idAlt ?: ""
}

data class VillaCreateNoticeRequest(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String
)
