package com.collatzinc.androidunittesting.presentation.screen.login

import androidx.annotation.StringRes
import com.collatzinc.androidunittesting.data.network.ApiError

sealed interface LoginUiState {

    data class Initial(

        val username: String = "",
        @StringRes val usernameError: Int? = null,

        val password: String = "",
        @StringRes val passwordError: Int? = null,
        val isPasswordVisible: Boolean = false,

        val isLoading: Boolean = false,
        val apiError: ApiError? = null,
    ) : LoginUiState

    data object LoggedIn : LoginUiState
}