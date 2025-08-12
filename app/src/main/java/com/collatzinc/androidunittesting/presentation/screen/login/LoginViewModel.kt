package com.collatzinc.androidunittesting.presentation.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collatzinc.androidunittesting.common.validators.ValidatorFactory
import com.collatzinc.androidunittesting.common.validators.ValidatorParam
import com.collatzinc.androidunittesting.data.network.RequestBodyHelper
import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validatorFactory: ValidatorFactory,
    private val requestBodyHelper: RequestBodyHelper
) : ViewModel() {

    private val _loginUiState =
        MutableStateFlow<LoginUiState>(LoginUiState.Initial())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private fun updateState(update: (LoginUiState.Initial) -> LoginUiState.Initial) {
        _loginUiState.value = (_loginUiState.value as? LoginUiState.Initial)?.let(update)
            ?: _loginUiState.value
    }


    fun onEvent(uiEvent: LoginUiEvent) = viewModelScope.launch {
        when (uiEvent) {
            is LoginUiEvent.UsernameChanged -> {
                updateState {
                    it.copy(
                        username = uiEvent.username,
                        usernameError = null
                    )
                }

            }

            is LoginUiEvent.PasswordChanged -> {
                updateState {
                    it.copy(
                        password = uiEvent.password,
                        passwordError = null
                    )
                }
            }

            LoginUiEvent.ClickOnKeyboardNext -> {
                areInputsValid()
            }

            LoginUiEvent.ClickOnKeyboardDone -> {
                if (areInputsValid()) {
                    login()
                }
            }

            LoginUiEvent.ClickOnLogin -> {
                if (areInputsValid()) {
                    login()
                }
            }

            LoginUiEvent.DismissApiError -> {
                updateState {
                    it.copy(apiError = null)
                }
            }
            LoginUiEvent.PasswordEyeToggleClick -> {
                updateState {
                    it.copy(isPasswordVisible = it.isPasswordVisible.not())
                }
            }
        }

    }


    private fun areInputsValid(): Boolean {
        val ui = _loginUiState.value as? LoginUiState.Initial ?: return false

        val usernameError =
            validatorFactory.get(ValidatorParam.USERNAME).validate(ui.username.trim())
        if (usernameError.isValid.not()) {
            updateState {
                it.copy(usernameError = usernameError.errorMessage)
            }
            return false
        }
        val passwordError =
            validatorFactory.get(ValidatorParam.PASSWORD).validate(ui.password.trim())
        if (passwordError.isValid.not()) {
            updateState {
                it.copy(passwordError = passwordError.errorMessage)
            }
            return false
        }

        return true

    }

    private fun login() = viewModelScope.launch {
        val ui = _loginUiState.value as? LoginUiState.Initial ?: return@launch
        updateState {
            it.copy(isLoading = true)
        }


        loginUseCase(
            requestBodyHelper.getLoginProperty(
                username = ui.username.trim(),
                password = ui.password.trim()
            )
        ).onEach { apiState ->
            when (apiState) {
                is ResponseState.Success -> {
                    _loginUiState.value = LoginUiState.LoggedIn
                }

                is ResponseState.Error -> {
                    updateState { it.copy(isLoading = false, apiError = apiState.apiError) }
                }
            }
        }.launchIn(viewModelScope)

    }

}