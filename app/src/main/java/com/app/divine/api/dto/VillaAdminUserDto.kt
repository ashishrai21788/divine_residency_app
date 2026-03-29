package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** Admin list users — Mongo lean may expose _id. */
data class VillaAdminUserDto(
    @SerializedName("_id") val mongoId: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("mobile") val mobile: String? = null,
) {
    fun displayId(): String = id ?: mongoId ?: "—"
}
