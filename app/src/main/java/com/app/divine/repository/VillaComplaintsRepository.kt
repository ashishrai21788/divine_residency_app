package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.toApiResult
import com.app.divine.api.dto.VillaComplaintCreateRequest
import com.app.divine.api.dto.VillaComplaintDto
import com.app.divine.api.villa.VillaComplaintsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaComplaintsRepository(private val api: VillaComplaintsApi) {

    fun createComplaint(
        request: VillaComplaintCreateRequest,
        onResult: (ApiResult<VillaComplaintDto>) -> Unit
    ) {
        onResult(ApiResult.Loading)
        api.createComplaint(request).enqueue(object : Callback<VillaComplaintDto> {
            override fun onResponse(call: Call<VillaComplaintDto>, response: Response<VillaComplaintDto>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<VillaComplaintDto>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    fun getMyComplaints(societyId: String, onResult: (ApiResult<List<VillaComplaintDto>>) -> Unit) {
        onResult(ApiResult.Loading)
        api.getMyComplaints(societyId).enqueue(object : Callback<List<VillaComplaintDto>> {
            override fun onResponse(call: Call<List<VillaComplaintDto>>, response: Response<List<VillaComplaintDto>>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<List<VillaComplaintDto>>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }
}
