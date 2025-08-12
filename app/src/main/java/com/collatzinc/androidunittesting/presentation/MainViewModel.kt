package com.collatzinc.androidunittesting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collatzinc.androidunittesting.domain.usecase.GetTokenUseCase
import com.collatzinc.androidunittesting.domain.usecase.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        viewModelScope.launch {
            val token = getTokenUseCase()
            _isLoggedIn.value = !token.isNullOrBlank()
        }
    }

    fun onEvent(uiEvent: MainUiEvent){
        when(uiEvent){
            MainUiEvent.GotoProfile -> {
                _isLoggedIn.value = true
            }
            MainUiEvent.Logout -> {
                logout()
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUserUseCase()
            _isLoggedIn.value = false
        }
    }

}