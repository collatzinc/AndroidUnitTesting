package com.collatzinc.androidunittesting.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

inline fun <T, R> safeApiCall(
    crossinline apiCall: suspend () -> Response<T>,
    errorMapper: ErrorMapper,
    crossinline onSuccess: (T?) -> R?
): Flow<ResponseState<R>> = flow {
    try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            emit(ResponseState.Success(onSuccess(response.body())))
        } else {
            emit(ResponseState.Error(errorMapper.map(response.code(), response.errorBody())))
        }
    } catch (throwable: Throwable) {
        emit(ResponseState.Error(errorMapper.map(throwable)))
    }
}