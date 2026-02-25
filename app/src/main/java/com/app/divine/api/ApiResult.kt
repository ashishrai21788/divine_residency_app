package com.app.divine.api

import retrofit2.Response

/**
 * Wrapper for API results: success with data, or failure with error message/code.
 * Use in repositories and ViewModels for consistent UI state.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

@Suppress("UNCHECKED_CAST")
fun <T> Response<T>.toApiResult(): ApiResult<T> {
    return if (isSuccessful) {
        body()?.let { ApiResult.Success(it) } ?: ApiResult.Success(Unit as T)
    } else {
        ApiResult.Error(message = errorBody()?.string() ?: "Request failed", code = code())
    }
}

fun <T> ApiResult<T>.dataOrNull(): T? = (this as? ApiResult.Success)?.data
