package com.app.divine.api.villa

import com.app.divine.api.dto.VillaGuardCheckInRequest
import com.app.divine.api.dto.VillaGuardCheckOutRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface VillaGuardAttendanceApi {

    @POST("guard/attendance/check-in")
    fun checkIn(@Body body: VillaGuardCheckInRequest): Call<Unit>

    @POST("guard/attendance/check-out")
    fun checkOut(@Body body: VillaGuardCheckOutRequest): Call<Unit>
}
