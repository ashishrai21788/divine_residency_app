package com.app.divine.activity.login.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaAuthResponse
import com.app.divine.repository.VillaAuthRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class LoginViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val villaAuthRepository: VillaAuthRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _villaLoginResult = MutableLiveData<ApiResult<VillaAuthResponse>>()
    val villaLoginResult: LiveData<ApiResult<VillaAuthResponse>> = _villaLoginResult

    /** Login with email, phone number, or user ID. Detects type and calls the appropriate API. */
    fun callLoginApi(identifier: String, password: String) {
        val trimmed = identifier.trim()
        when {
            trimmed.contains("@") ->
                villaAuthRepository.login(email = trimmed, password = password) { _villaLoginResult.postValue(it) }
            trimmed.all { it.isDigit() || it == '+' || it == ' ' || it == '-' } ->
                villaAuthRepository.login(mobile = trimmed.filter { it.isDigit() || it == '+' }, password = password) { _villaLoginResult.postValue(it) }
            else ->
                villaAuthRepository.login(username = trimmed, password = password) { _villaLoginResult.postValue(it) }
        }
    }

    fun callLoginApiWithMobile(mobile: String, password: String) {
        villaAuthRepository.login(mobile = mobile, password = password) { result ->
            _villaLoginResult.postValue(result)
        }
    }

    fun callLoginApiWithUsername(username: String, password: String) {
        villaAuthRepository.login(username = username, password = password) { result ->
            _villaLoginResult.postValue(result)
        }
    }
}