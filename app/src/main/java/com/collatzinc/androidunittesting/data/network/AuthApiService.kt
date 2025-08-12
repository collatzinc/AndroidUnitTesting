package com.collatzinc.androidunittesting.data.network

import com.collatzinc.androidunittesting.data.model.LoginDTO
import com.collatzinc.androidunittesting.data.model.RefreshTokenDTO
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: JsonObject): Response<LoginDTO>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: JsonObject): Response<RefreshTokenDTO>

}
