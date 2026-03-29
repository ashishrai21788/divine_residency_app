package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.toApiResult
import com.app.divine.api.dto.VillaAuthResponse
import com.app.divine.api.dto.VillaChangePasswordRequest
import com.app.divine.api.dto.VillaFcmTokenRequest
import com.app.divine.api.dto.VillaLoginRequest
import com.app.divine.api.dto.VillaRefreshTokenRequest
import com.app.divine.api.dto.VillaUserDto
import com.app.divine.api.villa.VillaAuthApi
import com.app.core.dagger.preference.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaAuthRepository(
    private val api: VillaAuthApi,
    private val appPreferences: AppPreferences
) {

    fun login(
        email: String? = null,
        username: String? = null,
        mobile: String? = null,
        password: String,
        onResult: (ApiResult<VillaAuthResponse>) -> Unit
    ) {
        onResult(ApiResult.Loading)
        val request = VillaLoginRequest(email = email, username = username, mobile = mobile, password = password)
        api.login(request).enqueue(object : Callback<VillaAuthResponse> {
            override fun onResponse(call: Call<VillaAuthResponse>, response: Response<VillaAuthResponse>) {
                onResult(response.toApiResult())
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    val jwt = body.accessTokenOrToken()
                    if (jwt.isNotEmpty()) {
                        appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_ACCESS_TOKEN_KEY, jwt)
                    }
                    body.refreshToken?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_REFRESH_TOKEN_KEY, it) }
                    body.roleOrFromUser()?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_USER_ROLE_KEY, it) }
                    body.user?.societyId?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY, it) }
                    body.user?.id?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_USER_ID_KEY, it.toString()) }
                    body.user?.name?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_USER_NAME_KEY, it) }
                    body.user?.villaId?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_VILLA_ID_KEY, it) }
                    body.user?.floorId?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_FLOOR_ID_KEY, it) }
                    body.user?.gateId?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_GATE_ID_KEY, it) }
                    appPreferences.setLogin(true)
                }
            }
            override fun onFailure(call: Call<VillaAuthResponse>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    fun refreshToken(onResult: (ApiResult<VillaAuthResponse>) -> Unit) {
        val refresh = appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_REFRESH_TOKEN_KEY)
        if (refresh.isEmpty()) {
            onResult(ApiResult.Error("No refresh token"))
            return
        }
        api.refreshToken(VillaRefreshTokenRequest(refresh)).enqueue(object : Callback<VillaAuthResponse> {
            override fun onResponse(call: Call<VillaAuthResponse>, response: Response<VillaAuthResponse>) {
                onResult(response.toApiResult())
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.accessToken?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_ACCESS_TOKEN_KEY, it) }
                    response.body()?.refreshToken?.let { appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_REFRESH_TOKEN_KEY, it) }
                }
            }
            override fun onFailure(call: Call<VillaAuthResponse>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    fun changePassword(currentPassword: String, newPassword: String, onResult: (ApiResult<Unit>) -> Unit) {
        api.changePassword(VillaChangePasswordRequest(currentPassword, newPassword)).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                onResult(if (response.isSuccessful) ApiResult.Success(Unit) else ApiResult.Error(response.message() ?: "Failed"))
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    fun getProfile(onResult: (ApiResult<VillaUserDto>) -> Unit) {
        api.getProfile().enqueue(object : Callback<VillaUserDto> {
            override fun onResponse(call: Call<VillaUserDto>, response: Response<VillaUserDto>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<VillaUserDto>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    /** Send FCM token to backend after login / on token refresh. */
    fun registerFcmToken(token: String) {
        api.registerFcmToken(VillaFcmTokenRequest(token)).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    android.util.Log.e("VillaAuthRepository", "registerFcmToken failed: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                android.util.Log.e("VillaAuthRepository", "registerFcmToken error", t)
            }
        })
    }

    /**
     * Clears local session. If [fcmTokenForThisDevice] is non-blank, unregisters it on the server first
     * (other devices keep receiving pushes). Prefs are cleared after the API call completes or if no token.
     */
    fun logout(fcmTokenForThisDevice: String? = null) {
        val clearLocalSession = {
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_ACCESS_TOKEN_KEY, "")
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_REFRESH_TOKEN_KEY, "")
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_USER_ROLE_KEY, "")
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY, "")
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_USER_ID_KEY, "")
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_USER_NAME_KEY, "")
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_VILLA_ID_KEY, "")
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_FLOOR_ID_KEY, "")
            appPreferences.setAllPreferenceTypeString(AppPreferences.VILLA_GATE_ID_KEY, "")
            appPreferences.setLogin(false)
        }
        val token = fcmTokenForThisDevice?.trim().orEmpty()
        if (token.isNotEmpty()) {
            api.removeFcmToken(VillaFcmTokenRequest(token)).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    clearLocalSession()
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    android.util.Log.e("VillaAuthRepository", "removeFcmToken error", t)
                    clearLocalSession()
                }
            })
        } else {
            clearLocalSession()
        }
    }
}
