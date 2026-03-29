package com.app.divine.villa

import com.app.divine.api.dto.VillaVisitorLogDto

sealed class RegisterVisitorOutcome {
    object Idle : RegisterVisitorOutcome()
    object Loading : RegisterVisitorOutcome()
    data class RemoteSuccess(val log: VillaVisitorLogDto) : RegisterVisitorOutcome()
    data object QueuedOffline : RegisterVisitorOutcome()
    data class Failed(val message: String) : RegisterVisitorOutcome()
}
