package com.collatzinc.androidunittesting.data.network

import com.collatzinc.androidunittesting.data.local.AuthLocalDataSource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticatorMutex @Inject constructor(
    private val authApiService: AuthApiService,
    private val localDataSource: AuthLocalDataSource,
    private val requestBodyHelper: RequestBodyHelper
) : Authenticator {

    private val tokenRefreshMutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            tokenRefreshMutex.withLock {

                val newAccessToken = localDataSource.getAccessToken()
                val requestToken =
                    response.request.header("Authorization")?.removePrefix("Bearer ")

                if (newAccessToken != null && newAccessToken != requestToken) {
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                }

                val refreshToken = localDataSource.getRefreshToken() ?: return@runBlocking null


                try {
                    val tokenResponse = authApiService.refreshToken(
                        requestBodyHelper.getRefreshTokenProperty(
                            refreshToken
                        )
                    )
                    if (tokenResponse.isSuccessful && tokenResponse.body() != null) {

                        val newToken = tokenResponse.body()?.accessToken ?: return@runBlocking null

                        localDataSource.setAccessToken(newToken)
                        localDataSource.setRefreshToken(tokenResponse.body()?.refreshToken)


                        return@runBlocking response.request.newBuilder()
                            .header("Authorization", "Bearer $newToken")
                            .build()
                    }
                } catch (_: Exception) {
                    // Ignored
                }
                localDataSource.setRefreshToken(null)
                return@runBlocking null
            }
        }
    }
}