package com.app.divine.api.dto

import com.google.gson.annotations.SerializedName

/** POST /users (admin) — UserID maps to `username` on backend. */
data class VillaCreateUserRequest(
    @SerializedName("role") val role: String,
    @SerializedName("username") val userId: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("society_id") val societyId: String? = null,
    @SerializedName("villa_id") val villaId: String? = null,
    @SerializedName("floor_id") val floorId: String? = null,
    @SerializedName("gate_id") val gateId: String? = null,
)
