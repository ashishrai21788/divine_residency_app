package com.app.divine.realtime

import com.google.gson.annotations.SerializedName

/**
 * Realtime socket event from backend.
 * Resident: visitor_request, visitor_status_update, sos_triggered
 * Guard: sos_triggered, visitor_status_update
 */
data class VillaSocketEvent(
    @SerializedName("event") val event: String,
    @SerializedName("data") val data: Map<String, Any?> = emptyMap()
) {
    fun visitorId(): String? = data["visitor_id"]?.toString()
    fun villaId(): String? = data["villa_id"]?.toString()
    fun floorId(): String? = data["floor_id"]?.toString()
    fun eventId(): String = "${event}_${visitorId()}_${data["timestamp"] ?: System.currentTimeMillis()}"
}
