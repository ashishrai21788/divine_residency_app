package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaCreateNoticeRequest
import com.app.divine.api.dto.VillaNoticeDto
import com.app.divine.repository.VillaNoticesRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class VillaNoticesViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val noticesRepository: VillaNoticesRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _listResult = MutableLiveData<ApiResult<List<VillaNoticeDto>>>()
    val listResult: LiveData<ApiResult<List<VillaNoticeDto>>> = _listResult

    private val _createResult = MutableLiveData<ApiResult<VillaNoticeDto>>()
    val createResult: LiveData<ApiResult<VillaNoticeDto>> = _createResult

    fun loadNotices(societyId: String) {
        noticesRepository.listNotices(societyId) { _listResult.postValue(it) }
    }

    fun createNotice(title: String, content: String) {
        noticesRepository.createNotice(VillaCreateNoticeRequest(title, content)) { _createResult.postValue(it) }
    }
}
