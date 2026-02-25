package com.app.divine.api.villa

import retrofit2.Call
import retrofit2.http.POST

interface VillaSOSApi {

    @POST("sos/trigger")
    fun triggerSOS(): Call<Unit>
}
