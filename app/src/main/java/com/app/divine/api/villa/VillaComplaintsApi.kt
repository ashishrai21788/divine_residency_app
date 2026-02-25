package com.app.divine.api.villa

import com.app.divine.api.dto.VillaComplaintCreateRequest
import com.app.divine.api.dto.VillaComplaintDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VillaComplaintsApi {

    @POST("complaints")
    fun createComplaint(@Body body: VillaComplaintCreateRequest): Call<VillaComplaintDto>

    @GET("complaints/my")
    fun getMyComplaints(@Query("society_id") societyId: String): Call<List<VillaComplaintDto>>
}
