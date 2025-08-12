package com.collatzinc.androidunittesting.data.network

sealed class ResponseState<out T> {
    data class Success<T>(val data: T? = null) : ResponseState<T>()
    data class Error(val apiError: ApiError) : ResponseState<Nothing>()
}

sealed class ApiError {
    data object Network : ApiError()
    data object Unauthorized : ApiError()
    data object Forbidden : ApiError()
    data class Unknown(val message: String? = null) : ApiError()
}