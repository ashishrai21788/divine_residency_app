package com.app.core.network

import com.app.core.dagger.preference.AppPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Adds Bearer token to Villa Society API requests when available.
 * Token is read from AppPreferences using [AppPreferences.VILLA_ACCESS_TOKEN_KEY].
 */
class VillaSocietyAuthInterceptor @Inject constructor(
    private val appPreferences: AppPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_ACCESS_TOKEN_KEY)
        val request = chain.request()
        val newRequest = if (token.isNotEmpty()) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()
        } else {
            request.newBuilder()
                .header("Content-Type", "application/json")
                .build()
        }
        return chain.proceed(newRequest)
    }
}
