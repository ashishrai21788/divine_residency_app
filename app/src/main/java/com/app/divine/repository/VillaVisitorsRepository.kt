package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.toApiResult
import com.app.divine.api.dto.VillaVisitorLogDto
import com.app.divine.api.dto.VillaVisitorRegisterRequest
import com.app.divine.api.villa.VillaVisitorsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaVisitorsRepository(private val api: VillaVisitorsApi) {

    fun registerVisitor(
        request: VillaVisitorRegisterRequest,
        onResult: (ApiResult<VillaVisitorLogDto>) -> Unit
    ) {
        onResult(ApiResult.Loading)
        api.registerVisitor(request).enqueue(callback(onResult))
    }

    fun approveVisitor(visitorId: String, onResult: (ApiResult<VillaVisitorLogDto>) -> Unit) {
        onResult(ApiResult.Loading)
        api.approveVisitor(visitorId).enqueue(callback(onResult))
    }

    fun rejectVisitor(visitorId: String, onResult: (ApiResult<VillaVisitorLogDto>) -> Unit) {
        onResult(ApiResult.Loading)
        api.rejectVisitor(visitorId).enqueue(callback(onResult))
    }

    fun markVisitorExit(visitorId: String, onResult: (ApiResult<VillaVisitorLogDto>) -> Unit) {
        onResult(ApiResult.Loading)
        api.markVisitorExit(visitorId).enqueue(callback(onResult))
    }

    fun getVisitorLogs(
        societyId: String?,
        floorId: String? = null,
        status: String? = null,
        onResult: (ApiResult<List<VillaVisitorLogDto>>) -> Unit
    ) {
        onResult(ApiResult.Loading)
        api.getVisitorLogs(societyId, floorId, status).enqueue(object : Callback<List<VillaVisitorLogDto>> {
            override fun onResponse(call: Call<List<VillaVisitorLogDto>>, response: Response<List<VillaVisitorLogDto>>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<List<VillaVisitorLogDto>>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    private fun callback(onResult: (ApiResult<VillaVisitorLogDto>) -> Unit) =
        object : Callback<VillaVisitorLogDto> {
            override fun onResponse(call: Call<VillaVisitorLogDto>, response: Response<VillaVisitorLogDto>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<VillaVisitorLogDto>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        }
}
