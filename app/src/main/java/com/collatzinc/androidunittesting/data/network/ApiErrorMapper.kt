package com.collatzinc.androidunittesting.data.network

import com.collatzinc.androidunittesting.data.model.ApiErrorResponse
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ApiErrorMapper @Inject constructor() : ErrorMapper {
    override fun map(throwable: Throwable): ApiError {
        return when (throwable) {
            is HttpException -> {
                handleErrorCode(errorCode = throwable.code(), message = throwable.message())
            }

            is IOException -> ApiError.Network
            is NullPointerException -> ApiError.Unknown("Unexpected null value encountered")

            else -> ApiError.Unknown(throwable.message)
        }
    }

    override fun map(errorCode: Int?, errorBody: ResponseBody?): ApiError {

        val json = Json { ignoreUnknownKeys = true }
        val rawJson = errorBody?.string().orEmpty()
        val parsedError = json.decodeFromString<ApiErrorResponse>(rawJson)

        val errorMessage = parsedError.message
        return handleErrorCode(
            errorCode = errorCode,
            message = errorMessage,
        )
    }

    private fun handleErrorCode(
        errorCode: Int?,
        message: String?,
    ): ApiError {
        return when (errorCode) {
            401 -> ApiError.Unauthorized
            403 -> {
                ApiError.Forbidden
            }

            else -> ApiError.Unknown(message)
        }
    }

}