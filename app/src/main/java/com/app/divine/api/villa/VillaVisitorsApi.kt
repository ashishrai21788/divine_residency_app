package com.app.divine.api.villa

import com.app.divine.api.dto.VillaVisitorLogDto
import com.app.divine.api.dto.VillaVisitorRegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VillaVisitorsApi {

    @POST("visitors")
    fun registerVisitor(@Body body: VillaVisitorRegisterRequest): Call<VillaVisitorLogDto>

    @POST("visitors/{id}/approve")
    fun approveVisitor(@Path("id") visitorId: String): Call<VillaVisitorLogDto>

    @POST("visitors/{id}/reject")
    fun rejectVisitor(@Path("id") visitorId: String): Call<VillaVisitorLogDto>

    @POST("visitors/{id}/exit")
    fun markVisitorExit(@Path("id") visitorId: String): Call<VillaVisitorLogDto>

    @GET("visitors/logs")
    fun getVisitorLogs(
        @Query("society_id") societyId: String? = null,
        @Query("floor_id") floorId: String? = null,
        @Query("status") status: String? = null
    ): Call<List<VillaVisitorLogDto>>
}
