package com.app.divine.activity.login.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.activity.login.api.LoginApiService
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModel(appDatabase: AppDatabase,
                     retrofit: Retrofit,
                     okHttpClient: OkHttpClient,
                     appPreferences: AppPreferences,
                     context: Context
): CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences,context) {
    private val _loginResult = MutableLiveData<JSONObject>()
    val loginResult: LiveData<JSONObject> = _loginResult

    fun callLoginApi(email: String, password: String) {
        val api = retrofit.create(LoginApiService::class.java)
        val body = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        api.login(body).enqueue(object : Callback<JSONObject> {
            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.postValue(response.body())
                } else {
                    _loginResult.postValue(JSONObject().apply { put("success", false); put("message", "Login failed") })
                }
            }
            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                _loginResult.postValue(JSONObject().apply { put("success", false); put("message", t.message ?: "Unknown error") })
            }
        })
    }
}