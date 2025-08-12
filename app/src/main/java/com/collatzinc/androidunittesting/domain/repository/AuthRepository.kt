package com.collatzinc.androidunittesting.domain.repository

import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.model.LoginData
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(requestParams: JsonObject): Flow<ResponseState<LoginData>>
    suspend fun getToken(): String?
    suspend fun clearToken()
}