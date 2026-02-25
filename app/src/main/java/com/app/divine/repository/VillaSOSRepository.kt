package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.toApiResult
import com.app.divine.api.villa.VillaSOSApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaSOSRepository(private val api: VillaSOSApi) {

    fun triggerSOS(onResult: (ApiResult<Unit>) -> Unit) {
        onResult(ApiResult.Loading)
        api.triggerSOS().enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }
}
