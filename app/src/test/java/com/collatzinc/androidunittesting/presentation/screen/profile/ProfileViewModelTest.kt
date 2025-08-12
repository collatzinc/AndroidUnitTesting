package com.collatzinc.androidunittesting.presentation.screen.profile

import app.cash.turbine.test
import com.collatzinc.androidunittesting.data.network.ApiError
import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.mapper.UserDataToUiMapper
import com.collatzinc.androidunittesting.domain.model.LoginData
import com.collatzinc.androidunittesting.domain.model.UserData
import com.collatzinc.androidunittesting.domain.usecase.GetUserDetailsUseCase
import com.collatzinc.androidunittesting.presentation.screen.login.LoginUiEvent
import com.collatzinc.androidunittesting.presentation.screen.login.LoginUiState
import com.collatzinc.androidunittesting.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    private val getUserDetailsUseCase: GetUserDetailsUseCase = mockk()
    private val userDataToUiMapper: UserDataToUiMapper = mockk()

    private lateinit var viewModel: ProfileViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {

        viewModel = ProfileViewModel(getUserDetailsUseCase, userDataToUiMapper)

    }

    @Test
    fun `initial loadUserDetails emit loading and then success`() = runTest {

        val domain = UserData(
            firstName = "Jane",
            lastName = "Smith",
            maidenName = "",
            age = 29,
            gender = "Male",
            email = "jane@example.com",
            phone = "+91 1234567890",
            username = "janesmith",
            birthDate = "1996-05-27",
            image = "avatar.png",
            bloodGroup = "O+"
        )

        val uiData = UserUiData(
            fullName = "Jane Smith",
            age = 29,
            gender = "Male",
            email = "jane@example.com",
            phone = "+91 1234567890",
            username = "@janesmith",
            birthDate = "1996-05-27",
            image = "avatar.png",
            bloodGroup = "O+"
        )

        coEvery { getUserDetailsUseCase() } returns flow {
            emit(ResponseState.Success(domain))
        }
        coEvery { userDataToUiMapper.map(domain) } returns uiData

        viewModel.profileUiState.test {

            //skip initial

            skipItems(1)

            val loading = awaitItem()
            assertEquals(true, loading.isLoading)

            val apiSuccess = awaitItem()
            assertEquals(false, apiSuccess.isLoading)
            assertEquals(uiData, apiSuccess.userUiData)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `click submit emit loading and then error`() = runTest {

        val error = ApiError.Unknown("Server Error")

        coEvery { getUserDetailsUseCase() } returns flow {
            emit(ResponseState.Error(error))
        }


        viewModel.profileUiState.test {

            //skip initial
            skipItems(1)

            val loading = awaitItem()
            assertEquals(true, loading.isLoading)

            val apiError = awaitItem()
            assertEquals(false, apiError.isLoading)
            assertEquals(true, apiError.apiError != null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `DismissApiError clears apiError from state`() = runTest {

        val error = ApiError.Unknown("Server Error")

        coEvery { getUserDetailsUseCase() } returns flow {
            emit(ResponseState.Error(error))
        }

        viewModel.profileUiState.test {

            //skip initial + loading
            skipItems(2)


            val apiError = awaitItem()
            assertEquals(false, apiError.isLoading)
            assertEquals(true, apiError.apiError != null)

            //Trigger DismissApiError event
            viewModel.onEvent(ProfileUiEvent.DismissApiError)
            val cleared = awaitItem()
            assertEquals(null, cleared.apiError)

            cancelAndIgnoreRemainingEvents()
        }
    }


}