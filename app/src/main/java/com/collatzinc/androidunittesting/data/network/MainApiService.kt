package com.collatzinc.androidunittesting.data.network

import com.collatzinc.androidunittesting.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.GET

interface MainApiService {

    @GET("auth/me")
    suspend fun userDetails(): Response<UserDTO>
}
