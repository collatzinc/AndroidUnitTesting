package com.collatzinc.androidunittesting.presentation

import app.cash.turbine.test
import com.collatzinc.androidunittesting.domain.usecase.GetTokenUseCase
import com.collatzinc.androidunittesting.domain.usecase.LogoutUserUseCase
import com.collatzinc.androidunittesting.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


class MainViewModelTest {

    private val getTokenUseCase: GetTokenUseCase = mockk()
    private val logoutUserUseCase: LogoutUserUseCase = mockk(relaxed = true)

    private lateinit var viewModel: MainViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initial valid token updates ui state`() = runTest {


        coEvery { getTokenUseCase() } returns "token"

        viewModel = MainViewModel(getTokenUseCase, logoutUserUseCase)

        advanceUntilIdle()
        assertEquals(true, viewModel.isLoggedIn.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initial invalid token updates ui state`() = runTest {


        coEvery { getTokenUseCase() } returns ""

        viewModel = MainViewModel(getTokenUseCase, logoutUserUseCase)

        advanceUntilIdle()
        assertEquals(false, viewModel.isLoggedIn.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GotoProfile updates ui state`() = runTest {

        coEvery { getTokenUseCase() } returns ""

        viewModel = MainViewModel(getTokenUseCase, logoutUserUseCase)

        viewModel.isLoggedIn.test {
            //skip initial
            skipItems(1)

            //Trigger GotoProfile event
            viewModel.onEvent(MainUiEvent.GotoProfile)

            val state = awaitItem()
            assertEquals(true,state)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Logout updates ui state`() = runTest {

        coEvery { getTokenUseCase() } returns "token"

        viewModel = MainViewModel(getTokenUseCase, logoutUserUseCase)

        viewModel.isLoggedIn.test {
            //skip initial + getTokenUseCase
            skipItems(2)

            //Trigger Logout event
            viewModel.onEvent(MainUiEvent.Logout)

            val state = awaitItem()
            assertEquals(false,state)
            coVerify { logoutUserUseCase() }
        }
    }

}