package com.collatzinc.androidunittesting.domain.repository

import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.model.LoginData
import com.collatzinc.androidunittesting.domain.model.UserData
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getDetails(): Flow<ResponseState<UserData>>
}