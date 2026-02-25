package com.app.divine.realtime

import androidx.fragment.app.Fragment

/**
 * Implement in fragments that should refresh when a realtime socket event is received.
 * Activity will call [onRealtimeEvent] when an event relevant to the current role is received.
 */
interface RealtimeRefresh {
    fun onRealtimeEvent(event: VillaSocketEvent)
}

fun Fragment.asRealtimeRefresh(): RealtimeRefresh? = this as? RealtimeRefresh
