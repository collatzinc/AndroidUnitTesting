package com.collatzinc.androidunittesting.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.mapper.UserDataToUiMapper
import com.collatzinc.androidunittesting.domain.usecase.GetUserDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val userDataToUiMapper: UserDataToUiMapper
) : ViewModel() {

    private val _profileUiState =
        MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()


    private fun updateProfileState(update: (ProfileUiState) -> ProfileUiState) {
        _profileUiState.value = _profileUiState.value.let(update)
    }

    init {
        viewModelScope.launch {
            launch { loadUserDetails() }
        }
    }


    fun onEvent(uiEvent: ProfileUiEvent) = viewModelScope.launch {
        when (uiEvent) {
            ProfileUiEvent.DismissApiError -> {
                updateProfileState {
                    it.copy(apiError = null)
                }
            }

        }
    }

    private fun loadUserDetails() = viewModelScope.launch {
        updateProfileState {
            it.copy(isLoading = true)
        }
        getUserDetailsUseCase(
        ).onEach { apiState ->
            when (apiState) {
                is ResponseState.Success -> {
                    val data = apiState.data
                    data?.let { mData ->
                        updateProfileState {
                            it.copy(isLoading = false, userUiData = userDataToUiMapper.map(mData))
                        }
                    }

                }

                is ResponseState.Error -> {
                    updateProfileState { it.copy(isLoading = false, apiError = apiState.apiError) }
                }
            }
        }.launchIn(viewModelScope)
    }


}