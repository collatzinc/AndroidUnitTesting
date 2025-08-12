package com.collatzinc.androidunittesting.data.network

class ApiErrorException(val apiError: ApiError) : Throwable(apiError.toString())