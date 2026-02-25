package com.app.divine.activity.login.api

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LoginApiService {
    @POST("login")
    fun login(@Body body: JSONObject): Call<JSONObject>

    @POST("add")
    fun addRecord(@Body body: JSONObject): Call<JSONObject>

    @PUT("edit/{id}")
    fun editRecord(@Path("id") id: String, @Body body: JSONObject): Call<JSONObject>

    @DELETE("delete/{id}")
    fun deleteRecord(@Path("id") id: String): Call<JSONObject>
} 