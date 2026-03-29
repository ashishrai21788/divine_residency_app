package com.app.divine.realtime

import com.google.gson.annotations.SerializedName

/**
 * Realtime event from the NestJS Socket.IO gateway.
 * Payload is the server emit body (e.g. full visitor document for `visitor_request`), not an `{event,data}` wrapper.
 */
data class VillaSocketEvent(
    @SerializedName("event") val event: String,
    @SerializedName("data") val data: Map<String, Any?> = emptyMap()
) {
    fun visitorId(): String? =
        data["visitor_id"]?.toString() ?: data["_id"]?.toString()
    fun villaId(): String? = data["villa_id"]?.toString()
    fun floorId(): String? = data["floor_id"]?.toString()
    fun eventId(): String = "${event}_${visitorId()}_${data["timestamp"] ?: System.currentTimeMillis()}"
}
