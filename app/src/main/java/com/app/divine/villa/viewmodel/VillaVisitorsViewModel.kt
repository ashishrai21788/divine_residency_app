package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaVisitorLogDto
import com.app.divine.api.dto.VillaVisitorRegisterRequest
import com.app.divine.repository.VillaVisitorsRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class VillaVisitorsViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val visitorsRepository: VillaVisitorsRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _registerResult = MutableLiveData<ApiResult<VillaVisitorLogDto>>()
    val registerResult: LiveData<ApiResult<VillaVisitorLogDto>> = _registerResult

    private val _actionResult = MutableLiveData<ApiResult<VillaVisitorLogDto>>()
    val actionResult: LiveData<ApiResult<VillaVisitorLogDto>> = _actionResult

    private val _logsResult = MutableLiveData<ApiResult<List<VillaVisitorLogDto>>>()
    val logsResult: LiveData<ApiResult<List<VillaVisitorLogDto>>> = _logsResult

    fun registerVisitor(request: VillaVisitorRegisterRequest) {
        visitorsRepository.registerVisitor(request) { _registerResult.postValue(it) }
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
