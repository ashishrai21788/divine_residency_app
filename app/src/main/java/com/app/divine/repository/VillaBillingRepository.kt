package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.toApiResult
import com.app.divine.api.dto.VillaBillDto
import com.app.divine.api.dto.VillaPaymentDto
import com.app.divine.api.villa.VillaBillingApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaBillingRepository(private val api: VillaBillingApi) {

    fun getMyBills(societyId: String, onResult: (ApiResult<List<VillaBillDto>>) -> Unit) {
        onResult(ApiResult.Loading)
        api.getMyBills(societyId).enqueue(listCallback(onResult))
    }

    fun getMyPendingBills(societyId: String, onResult: (ApiResult<List<VillaBillDto>>) -> Unit) {
        onResult(ApiResult.Loading)
        api.getMyPendingBills(societyId).enqueue(listCallback(onResult))
    }

    fun getMyPayments(societyId: String, onResult: (ApiResult<List<VillaPaymentDto>>) -> Unit) {
        onResult(ApiResult.Loading)
        api.getMyPayments(societyId).enqueue(object : Callback<List<VillaPaymentDto>> {
            override fun onResponse(call: Call<List<VillaPaymentDto>>, response: Response<List<VillaPaymentDto>>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<List<VillaPaymentDto>>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    private fun listCallback(onResult: (ApiResult<List<VillaBillDto>>) -> Unit) =
        object : Callback<List<VillaBillDto>> {
            override fun onResponse(call: Call<List<VillaBillDto>>, response: Response<List<VillaBillDto>>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<List<VillaBillDto>>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        }
}
