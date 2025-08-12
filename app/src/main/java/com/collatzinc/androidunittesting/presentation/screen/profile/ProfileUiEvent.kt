package com.collatzinc.androidunittesting.presentation.screen.profile

sealed class ProfileUiEvent {
    data object DismissApiError : ProfileUiEvent()

}
