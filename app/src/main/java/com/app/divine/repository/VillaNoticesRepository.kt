package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaCreateNoticeRequest
import com.app.divine.api.dto.VillaNoticeDto
import com.app.divine.api.toApiResult
import com.app.divine.api.villa.VillaNoticesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaNoticesRepository(private val api: VillaNoticesApi) {

    fun listNotices(societyId: String, onResult: (ApiResult<List<VillaNoticeDto>>) -> Unit) {
        onResult(ApiResult.Loading)
        api.listNotices(societyId).enqueue(object : Callback<List<VillaNoticeDto>> {
            override fun onResponse(
                call: Call<List<VillaNoticeDto>>,
                response: Response<List<VillaNoticeDto>>
            ) {
                onResult(response.toApiResult())
            }

            override fun onFailure(call: Call<List<VillaNoticeDto>>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    fun createNotice(body: VillaCreateNoticeRequest, onResult: (ApiResult<VillaNoticeDto>) -> Unit) {
        onResult(ApiResult.Loading)
        api.createNotice(body).enqueue(object : Callback<VillaNoticeDto> {
            override fun onResponse(call: Call<VillaNoticeDto>, response: Response<VillaNoticeDto>) {
                onResult(response.toApiResult())
            }

            override fun onFailure(call: Call<VillaNoticeDto>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }
}
