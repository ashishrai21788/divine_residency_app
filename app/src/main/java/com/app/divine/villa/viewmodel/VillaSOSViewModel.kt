package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.repository.VillaSOSRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class VillaSOSViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val sosRepository: VillaSOSRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _triggerResult = MutableLiveData<ApiResult<Unit>>()
    val triggerResult: LiveData<ApiResult<Unit>> = _triggerResult

    fun triggerSOS() {
        sosRepository.triggerSOS { _triggerResult.postValue(it) }
    }
}
