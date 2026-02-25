package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.toApiResult
import com.app.divine.api.dto.VillaGuardCheckInRequest
import com.app.divine.api.dto.VillaGuardCheckOutRequest
import com.app.divine.api.villa.VillaGuardAttendanceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaGuardAttendanceRepository(private val api: VillaGuardAttendanceApi) {

    fun checkIn(checkInPhotoUrl: String? = null, onResult: (ApiResult<Unit>) -> Unit) {
        onResult(ApiResult.Loading)
        api.checkIn(VillaGuardCheckInRequest(checkInPhotoUrl)).enqueue(unitCallback(onResult))
    }

    fun checkOut(checkOutPhotoUrl: String? = null, onResult: (ApiResult<Unit>) -> Unit) {
        onResult(ApiResult.Loading)
        api.checkOut(VillaGuardCheckOutRequest(checkOutPhotoUrl)).enqueue(unitCallback(onResult))
    }

    private fun unitCallback(onResult: (ApiResult<Unit>) -> Unit) =
        object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        }
}
