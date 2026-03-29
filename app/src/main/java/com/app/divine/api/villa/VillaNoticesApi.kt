package com.app.divine.api.villa

import com.app.divine.api.dto.VillaCreateNoticeRequest
import com.app.divine.api.dto.VillaNoticeDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VillaNoticesApi {

    @GET("notices")
    fun listNotices(
        @Query("society_id") societyId: String,
        @Query("activeOnly") activeOnly: String = "true"
    ): Call<List<VillaNoticeDto>>

    @POST("notices")
    fun createNotice(@Body body: VillaCreateNoticeRequest): Call<VillaNoticeDto>
}
