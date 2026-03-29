package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.core.dagger.coredao.PendingGuardVisitorDao
import com.app.core.dagger.coreentity.PendingGuardVisitorEntity
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaVisitorLogDto
import com.app.divine.api.dto.VillaVisitorRegisterRequest
import com.app.divine.repository.VillaVisitorsRepository
import com.app.divine.sync.GuardVisitorSyncWorker
import com.app.divine.villa.RegisterVisitorOutcome
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.UUID

class VillaVisitorsViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val visitorsRepository: VillaVisitorsRepository,
    private val pendingGuardVisitorDao: PendingGuardVisitorDao,
    private val gson: Gson,
    private val appContext: Context,
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    companion object {
        const val SYNC_STATUS_PENDING = "PENDING"
        const val SYNC_STATUS_FAILED = "FAILED"
    }

    private val _registerResult = MutableLiveData<ApiResult<VillaVisitorLogDto>>()
    val registerResult: LiveData<ApiResult<VillaVisitorLogDto>> = _registerResult

    private val _registerOutcome = MutableLiveData<RegisterVisitorOutcome>(RegisterVisitorOutcome.Idle)
    val registerOutcome: LiveData<RegisterVisitorOutcome> = _registerOutcome

    fun offlinePendingLiveData(): LiveData<Int> =
        pendingGuardVisitorDao.observePendingCount(SYNC_STATUS_PENDING)

    private val _actionResult = MutableLiveData<ApiResult<VillaVisitorLogDto>>()
    val actionResult: LiveData<ApiResult<VillaVisitorLogDto>> = _actionResult

    private val _logsResult = MutableLiveData<ApiResult<List<VillaVisitorLogDto>>>()
    val logsResult: LiveData<ApiResult<List<VillaVisitorLogDto>>> = _logsResult

    fun registerVisitor(request: VillaVisitorRegisterRequest) {
        visitorsRepository.registerVisitor(request) { _registerResult.postValue(it) }
    }

    /**
     * Guard: submit visitor; on transport failure (no HTTP code) queue to Room and sync later.
     */
    fun registerVisitorOrQueue(request: VillaVisitorRegisterRequest) {
        _registerOutcome.value = RegisterVisitorOutcome.Loading
        visitorsRepository.registerVisitor(request) { r ->
            when (r) {
                is ApiResult.Success -> {
                    _registerResult.postValue(r)
                    _registerOutcome.postValue(RegisterVisitorOutcome.RemoteSuccess(r.data))
                }
                is ApiResult.Error -> {
                    val queueOffline = r.code == null
                    if (!queueOffline) {
                        _registerResult.postValue(r)
                        _registerOutcome.postValue(RegisterVisitorOutcome.Failed(r.message))
                        return@registerVisitor
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            val clientId = UUID.randomUUID().toString()
                            pendingGuardVisitorDao.insert(
                                PendingGuardVisitorEntity(
                                    clientRequestId = clientId,
                                    requestJson = gson.toJson(request),
                                    createdAt = System.currentTimeMillis(),
                                    syncStatus = SYNC_STATUS_PENDING
                                )
                            )
                            withContext(Dispatchers.Main) {
                                _registerOutcome.value = RegisterVisitorOutcome.QueuedOffline
                            }
                            GuardVisitorSyncWorker.enqueue(appContext)
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                _registerOutcome.value = RegisterVisitorOutcome.Failed(
                                    e.message ?: "Could not save offline"
                                )
                            }
                        }
                    }
                }
                is ApiResult.Loading -> {}
            }
        }
    }

    fun consumeRegisterOutcome() {
        _registerOutcome.value = RegisterVisitorOutcome.Idle
    }

    fun approveVisitor(visitorId: String) {
        visitorsRepository.approveVisitor(visitorId) { _actionResult.postValue(it) }
    }

    fun rejectVisitor(visitorId: String) {
        visitorsRepository.rejectVisitor(visitorId) { _actionResult.postValue(it) }
    }

    fun markVisitorExit(visitorId: String) {
        visitorsRepository.markVisitorExit(visitorId) { _actionResult.postValue(it) }
    }

    fun loadVisitorLogs(societyId: String?, floorId: String? = null, status: String? = null) {
        visitorsRepository.getVisitorLogs(societyId, floorId, status) { _logsResult.postValue(it) }
    }
}
