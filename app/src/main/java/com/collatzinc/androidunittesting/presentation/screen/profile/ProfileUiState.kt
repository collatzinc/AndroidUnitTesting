package com.collatzinc.androidunittesting.presentation.screen.profile

import com.collatzinc.androidunittesting.data.network.ApiError

data class ProfileUiState(
    val userUiData: UserUiData?=null,
    val isLoading: Boolean = false,
    val apiError: ApiError? = null,
)
