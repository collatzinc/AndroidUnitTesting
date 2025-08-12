package com.collatzinc.androidunittesting.data.repository

import com.collatzinc.androidunittesting.data.mapper.UserDTOToDomainMapper
import com.collatzinc.androidunittesting.data.network.ErrorMapper
import com.collatzinc.androidunittesting.data.network.MainApiService
import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.data.network.safeApiCall
import com.collatzinc.androidunittesting.domain.model.UserData
import com.collatzinc.androidunittesting.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val mainApiService: MainApiService,
    private val errorMapper: ErrorMapper,
    private val userDTOToDomainMapper: UserDTOToDomainMapper
) : UserRepository {
    override fun getDetails(): Flow<ResponseState<UserData>> {
        return safeApiCall(
            apiCall = { mainApiService.userDetails() },
            errorMapper = errorMapper,
            onSuccess = { dto ->
                dto?.let { userDTOToDomainMapper.map(dto) }
            }
        )
    }
}