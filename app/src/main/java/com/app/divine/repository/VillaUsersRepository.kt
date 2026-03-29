package com.app.divine.repository

import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaAdminUserDto
import com.app.divine.api.dto.VillaCreateUserRequest
import com.app.divine.api.toApiResult
import com.app.divine.api.villa.VillaUsersApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillaUsersRepository(private val api: VillaUsersApi) {

    fun listUsers(societyId: String?, onResult: (ApiResult<List<VillaAdminUserDto>>) -> Unit) {
        onResult(ApiResult.Loading)
        api.listUsers(societyId).enqueue(object : Callback<List<VillaAdminUserDto>> {
            override fun onResponse(
                call: Call<List<VillaAdminUserDto>>,
                response: Response<List<VillaAdminUserDto>>
            ) {
                onResult(response.toApiResult())
            }

            override fun onFailure(call: Call<List<VillaAdminUserDto>>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }

    fun createUser(body: VillaCreateUserRequest, onResult: (ApiResult<VillaAdminUserDto>) -> Unit) {
        onResult(ApiResult.Loading)
        api.createUser(body).enqueue(object : Callback<VillaAdminUserDto> {
            override fun onResponse(call: Call<VillaAdminUserDto>, response: Response<VillaAdminUserDto>) {
                onResult(response.toApiResult())
            }

            override fun onFailure(call: Call<VillaAdminUserDto>, t: Throwable) {
                onResult(ApiResult.Error(t.message ?: "Network error"))
            }
        })
    }
}
