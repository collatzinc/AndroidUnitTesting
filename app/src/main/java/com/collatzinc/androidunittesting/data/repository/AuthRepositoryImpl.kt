package com.collatzinc.androidunittesting.data.repository

import com.collatzinc.androidunittesting.data.local.AuthLocalDataSource
import com.collatzinc.androidunittesting.data.mapper.LoginDTOToDomainMapper
import com.collatzinc.androidunittesting.data.network.AuthApiService
import com.collatzinc.androidunittesting.data.network.ErrorMapper
import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.data.network.safeApiCall
import com.collatzinc.androidunittesting.domain.model.LoginData
import com.collatzinc.androidunittesting.domain.repository.AuthRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val localDataSource: AuthLocalDataSource,
    private val errorMapper: ErrorMapper,
    private val loginDTOToDomainMapper: LoginDTOToDomainMapper
) : AuthRepository {
    override fun login(requestParams: JsonObject): Flow<ResponseState<LoginData>> {
        return safeApiCall(
            apiCall = { apiService.login(requestParams) },
            errorMapper = errorMapper,
            onSuccess = { dto ->
                localDataSource.setAccessToken(dto?.accessToken)
                localDataSource.setAccessToken(dto?.refreshToken)
                dto?.let { loginDTOToDomainMapper.map(dto) }
            }
        )
    }

    override suspend fun getToken(): String? {
        return localDataSource.getAccessToken()
    }

    override suspend fun clearToken() {
        localDataSource.clear()
    }
}