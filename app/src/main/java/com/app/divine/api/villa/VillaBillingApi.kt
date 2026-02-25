package com.app.divine.api.villa

import com.app.divine.api.dto.VillaBillDto
import com.app.divine.api.dto.VillaPaymentDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/** Resident: my bills and payments. */
interface VillaBillingApi {

    @GET("my/bills")
    fun getMyBills(@Query("society_id") societyId: String): Call<List<VillaBillDto>>

    @GET("my/bills/pending")
    fun getMyPendingBills(@Query("society_id") societyId: String): Call<List<VillaBillDto>>

    @GET("my/payments")
    fun getMyPayments(@Query("society_id") societyId: String): Call<List<VillaPaymentDto>>
}
