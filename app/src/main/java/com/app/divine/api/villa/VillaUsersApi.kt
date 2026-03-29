package com.app.divine.api.villa

import com.app.divine.api.dto.VillaAdminUserDto
import com.app.divine.api.dto.VillaCreateUserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VillaUsersApi {

    @GET("users")
    fun listUsers(@Query("society_id") societyId: String?): Call<List<VillaAdminUserDto>>

    @POST("users")
    fun createUser(@Body body: VillaCreateUserRequest): Call<VillaAdminUserDto>
}
