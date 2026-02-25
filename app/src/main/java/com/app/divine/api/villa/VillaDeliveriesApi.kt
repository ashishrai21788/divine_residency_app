package com.app.divine.api.villa

import com.app.divine.api.dto.VillaDeliveryCreateRequest
import com.app.divine.api.dto.VillaDeliveryDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VillaDeliveriesApi {

    @POST("deliveries")
    fun createDelivery(@Body body: VillaDeliveryCreateRequest): Call<VillaDeliveryDto>

    @POST("deliveries/{id}/received")
    fun markReceived(@Path("id") deliveryId: String): Call<VillaDeliveryDto>

    @GET("deliveries")
    fun getDeliveries(
        @Query("floor_id") floorId: String? = null,
        @Query("society_id") societyId: String? = null
    ): Call<List<VillaDeliveryDto>>
}
