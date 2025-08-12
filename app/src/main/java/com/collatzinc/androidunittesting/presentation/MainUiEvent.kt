package com.collatzinc.androidunittesting.presentation

sealed class MainUiEvent {

    data object GotoProfile : MainUiEvent()
    data object Logout : MainUiEvent()

}