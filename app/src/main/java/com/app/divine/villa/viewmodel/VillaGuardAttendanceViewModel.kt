package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.repository.VillaGuardAttendanceRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class VillaGuardAttendanceViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val guardAttendanceRepository: VillaGuardAttendanceRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _checkInResult = MutableLiveData<ApiResult<Unit>>()
    val checkInResult: LiveData<ApiResult<Unit>> = _checkInResult

    private val _checkOutResult = MutableLiveData<ApiResult<Unit>>()
    val checkOutResult: LiveData<ApiResult<Unit>> = _checkOutResult

    fun checkIn(checkInPhotoUrl: String? = null) {
        guardAttendanceRepository.checkIn(checkInPhotoUrl) { _checkInResult.postValue(it) }
    }

    fun checkOut(checkOutPhotoUrl: String? = null) {
        guardAttendanceRepository.checkOut(checkOutPhotoUrl) { _checkOutResult.postValue(it) }
    }
}
