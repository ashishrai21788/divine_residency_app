package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaComplaintCreateRequest
import com.app.divine.api.dto.VillaComplaintDto
import com.app.divine.repository.VillaComplaintsRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class VillaComplaintsViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val complaintsRepository: VillaComplaintsRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _createResult = MutableLiveData<ApiResult<VillaComplaintDto>>()
    val createResult: LiveData<ApiResult<VillaComplaintDto>> = _createResult

    private val _myComplaintsResult = MutableLiveData<ApiResult<List<VillaComplaintDto>>>()
    val myComplaintsResult: LiveData<ApiResult<List<VillaComplaintDto>>> = _myComplaintsResult

    fun createComplaint(request: VillaComplaintCreateRequest) {
        complaintsRepository.createComplaint(request) { _createResult.postValue(it) }
    }

    fun loadMyComplaints(societyId: String) {
        complaintsRepository.getMyComplaints(societyId) { _myComplaintsResult.postValue(it) }
    }
}
