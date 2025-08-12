package com.collatzinc.androidunittesting.data.network

import okhttp3.ResponseBody

interface ErrorMapper {
    fun map(throwable: Throwable): ApiError
    fun map(errorCode: Int?, errorBody: ResponseBody?): ApiError
}