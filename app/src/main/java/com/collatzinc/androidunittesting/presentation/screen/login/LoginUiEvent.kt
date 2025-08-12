package com.collatzinc.androidunittesting.presentation.screen.login

sealed class LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    data object PasswordEyeToggleClick : LoginUiEvent()

    data object ClickOnLogin : LoginUiEvent()
    data object ClickOnKeyboardNext : LoginUiEvent()
    data object ClickOnKeyboardDone : LoginUiEvent()
    data object DismissApiError : LoginUiEvent()
}
