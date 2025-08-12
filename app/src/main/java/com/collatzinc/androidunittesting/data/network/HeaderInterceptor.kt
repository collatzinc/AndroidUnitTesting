package com.collatzinc.androidunittesting.data.network

import com.collatzinc.androidunittesting.data.local.AuthLocalDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val localDataSource: AuthLocalDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val token = localDataSource.getAccessToken()
        if(token.isNullOrBlank().not()){
            request.addHeader("Authorization", "Bearer $token")
        }
        request.addHeader("Content-Type", "application/json")

        return chain.proceed(request.build())
    }
}