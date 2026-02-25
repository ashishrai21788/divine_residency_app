package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.toApiResult
import com.app.divine.api.dto.VillaDeliveryCreateRequest
import com.app.divine.api.dto.VillaDeliveryDto
import com.app.divine.api.villa.VillaDeliveriesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaDeliveriesRepository(private val api: VillaDeliveriesApi) {

    fun createDelivery(
        request: VillaDeliveryCreateRequest,
        onResult: (ApiResult<VillaDeliveryDto>) -> Unit
    ) {
        onResult(ApiResult.Loading)
        api.createDelivery(request).enqueue(object : Callback<VillaDeliveryDto> {
            override fun onResponse(call: Call<VillaDeliveryDto>, response: Response<VillaDeliveryDto>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<VillaDeliveryDto>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    fun markReceived(deliveryId: String, onResult: (ApiResult<VillaDeliveryDto>) -> Unit) {
        onResult(ApiResult.Loading)
        api.markReceived(deliveryId).enqueue(object : Callback<VillaDeliveryDto> {
            override fun onResponse(call: Call<VillaDeliveryDto>, response: Response<VillaDeliveryDto>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<VillaDeliveryDto>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    fun getDeliveries(
        floorId: String? = null,
        societyId: String? = null,
        onResult: (ApiResult<List<VillaDeliveryDto>>) -> Unit
    ) {
        onResult(ApiResult.Loading)
        api.getDeliveries(floorId, societyId).enqueue(object : Callback<List<VillaDeliveryDto>> {
            override fun onResponse(call: Call<List<VillaDeliveryDto>>, response: Response<List<VillaDeliveryDto>>) {
                onResult(response.toApiResult())
            }
            override fun onFailure(call: Call<List<VillaDeliveryDto>>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }
}
