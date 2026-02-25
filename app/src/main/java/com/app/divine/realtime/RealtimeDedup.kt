package com.app.divine.realtime

import java.util.concurrent.ConcurrentHashMap

/**
 * Prevents duplicate handling when the same event is received via both socket and push.
 * Call markHandledBySocket() when a socket event is processed; call wasHandledBySocket() from FCM handler
 * to skip showing push if we already handled this event recently.
 */
object RealtimeDedup {

    private const val TTL_MS = 60_000L // 1 minute
    private val recentBySocket = ConcurrentHashMap<String, Long>()

    fun markHandledBySocket(event: VillaSocketEvent) {
        recentBySocket[event.eventId()] = System.currentTimeMillis()
        cleanup()
    }

    /** Call from FCM handler: if true, skip showing push to avoid duplicate. */
    fun wasHandledBySocket(type: String, visitorId: String?, dataTimestamp: Any?): Boolean {
        val key = "${type}_${visitorId}_${dataTimestamp ?: ""}"
        val at = recentBySocket[key] ?: return false
        if (System.currentTimeMillis() - at > TTL_MS) {
            recentBySocket.remove(key)
            return false
        }
        return true
    }

    private fun cleanup() {
        val now = System.currentTimeMillis()
        recentBySocket.entries.removeIf { (_, ts) -> now - ts > TTL_MS }
    }
}
