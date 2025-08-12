package com.collatzinc.androidunittesting.domain.usecase

import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.model.LoginData
import com.collatzinc.androidunittesting.domain.model.UserData
import com.collatzinc.androidunittesting.domain.repository.AuthRepository
import com.collatzinc.androidunittesting.domain.repository.UserRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(): Flow<ResponseState<UserData>> = repository.getDetails()

}