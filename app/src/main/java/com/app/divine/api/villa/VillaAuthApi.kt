package com.app.divine.api.villa

import com.app.divine.api.dto.VillaAuthResponse
import com.app.divine.api.dto.VillaChangePasswordRequest
import com.app.divine.api.dto.VillaFcmTokenRequest
import com.app.divine.api.dto.VillaLoginRequest
import com.app.divine.api.dto.VillaRefreshTokenRequest
import com.app.divine.api.dto.VillaUserDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

/** Villa Society Auth APIs. Base URL: http://localhost:3000/api/ */
interface VillaAuthApi {

    @POST("auth/login")
    fun login(@Body body: VillaLoginRequest): Call<VillaAuthResponse>

    @POST("auth/refresh-token")
    fun refreshToken(@Body body: VillaRefreshTokenRequest): Call<VillaAuthResponse>

    @POST("auth/change-password")
    fun changePassword(@Body body: VillaChangePasswordRequest): Call<Unit>

    @GET("auth/profile")
    fun getProfile(): Call<VillaUserDto>

    /** Register FCM token after login. Update on token refresh. */
    @POST("notifications/register")
    fun registerFcmToken(@Body body: VillaFcmTokenRequest): Call<Unit>

    /** Remove FCM token on logout. */
    @DELETE("notifications/token")
    fun removeFcmToken(): Call<Unit>
}
