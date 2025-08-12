package com.collatzinc.androidunittesting.domain.usecase

import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.model.LoginData
import com.collatzinc.androidunittesting.domain.repository.AuthRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(requestParams: JsonObject): Flow<ResponseState<LoginData>> =
        repository.login(requestParams)

}