package com.collatzinc.androidunittesting.domain.usecase

import com.collatzinc.androidunittesting.domain.repository.AuthRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(): String? {
        return repository.getToken()
    }
}