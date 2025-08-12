package com.collatzinc.androidunittesting.presentation.screen.login

import app.cash.turbine.test
import com.collatzinc.androidunittesting.common.validators.PasswordValidator
import com.collatzinc.androidunittesting.common.validators.UserNameValidator
import com.collatzinc.androidunittesting.common.validators.ValidatorFactory
import com.collatzinc.androidunittesting.common.validators.ValidatorParam
import com.collatzinc.androidunittesting.data.network.ApiError
import com.collatzinc.androidunittesting.data.network.RequestBodyHelper
import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.model.LoginData
import com.collatzinc.androidunittesting.domain.usecase.LoginUseCase
import com.collatzinc.androidunittesting.rule.MainDispatcherRule
import com.google.gson.JsonObject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LoginViewModelTest {
    private val loginUseCase: LoginUseCase = mockk()
    private val requestBodyHelper: RequestBodyHelper = mockk()
    private val validatorFactory: ValidatorFactory = mockk()
    private lateinit var dummyRequestJson: JsonObject

    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {

        dummyRequestJson = JsonObject()
        every { validatorFactory.get(ValidatorParam.PASSWORD) } returns PasswordValidator()
        every { validatorFactory.get(ValidatorParam.USERNAME) } returns UserNameValidator()

        viewModel = LoginViewModel(loginUseCase, validatorFactory, requestBodyHelper)

    }

    @Test
    fun `check initial username & password`() = runTest {

        viewModel.loginUiState.test {
            val state = awaitItem() as LoginUiState.Initial
            assertEquals("emilys", state.username)
            assertEquals("emilyspass", state.password)
            assertEquals(null, state.usernameError)
            assertEquals(null, state.passwordError)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `username changed updates state`() = runTest {

        viewModel.loginUiState.test {
            //skip initial
            skipItems(1)

            // Trigger UsernameChanged event
            viewModel.onEvent(LoginUiEvent.UsernameChanged("username"))
            val state = awaitItem() as LoginUiState.Initial
            assertEquals("username", state.username)
            assertEquals(null, state.usernameError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `password changed updates state`() = runTest {

        viewModel.loginUiState.test {
            //skip initial
            skipItems(1)

            // Trigger PasswordChanged event
            viewModel.onEvent(LoginUiEvent.PasswordChanged("password"))
            val state = awaitItem() as LoginUiState.Initial
            assertEquals("password", state.password)
            assertEquals(null, state.passwordError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `click on Login with invalid username shows error`() = runTest {

        viewModel.onEvent(LoginUiEvent.UsernameChanged(""))

        viewModel.loginUiState.test {
            //Skip initial + UsernameChanged
            skipItems(2)

            //Trigger ClickOnLogin event
            viewModel.onEvent(LoginUiEvent.ClickOnLogin)
            val state = awaitItem() as LoginUiState.Initial
            assertEquals(true, state.usernameError != null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `click on Login with invalid password shows error`() = runTest {

        viewModel.onEvent(LoginUiEvent.PasswordChanged("short12"))

        viewModel.loginUiState.test {
            //skip initial + PasswordChanged
            skipItems(2)

            //Trigger ClickOnLogin event
            viewModel.onEvent(LoginUiEvent.ClickOnLogin)
            val state = awaitItem() as LoginUiState.Initial
            assertEquals(true, state.passwordError!=null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `click on keyboard next with invalid username shows error`() = runTest {
        viewModel.onEvent(LoginUiEvent.UsernameChanged(""))

        viewModel.loginUiState.test {
            //Skip initial + UsernameChanged
            skipItems(2)

            //Trigger ClickOnKeyboardNext event
            viewModel.onEvent(LoginUiEvent.ClickOnKeyboardNext)
            val state = awaitItem() as LoginUiState.Initial
            assertEquals(true, state.usernameError != null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `click on keyboard done with invalid password shows error`() = runTest {
        viewModel.onEvent(LoginUiEvent.PasswordChanged("short12"))

        viewModel.loginUiState.test {
            //skip initial + PasswordChanged
            skipItems(2)

            //Trigger ClickOnKeyboardDone event
            viewModel.onEvent(LoginUiEvent.ClickOnKeyboardDone)
            val state = awaitItem() as LoginUiState.Initial
            assertEquals(true, state.passwordError != null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `password visibility toggles twice and returns to original state`() = runTest {

        viewModel.loginUiState.test {
            //skip initial
            skipItems(1)

            // Trigger PasswordChanged event
            viewModel.onEvent(LoginUiEvent.PasswordChanged("short"))
            val afterPasswordChanged = awaitItem() as LoginUiState.Initial
            assertEquals(false, afterPasswordChanged.isPasswordVisible)

            // Click eye toggle (1st time)
            viewModel.onEvent(LoginUiEvent.PasswordEyeToggleClick)
            val afterFirstToggle = awaitItem() as LoginUiState.Initial
            assertEquals(true, afterFirstToggle.isPasswordVisible)

            // Click eye toggle (2nd time)
            viewModel.onEvent(LoginUiEvent.PasswordEyeToggleClick)
            val afterSecondToggle = awaitItem() as LoginUiState.Initial
            assertEquals(false, afterSecondToggle.isPasswordVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `click on keyboard done emit loading and then success`() = runTest {

        val loginData = LoginData(
            username = "username",
            email = "email@test.com",
            firstName = "firstName",
            lastName = "lastName",
            gender = "gender",
            image = "image"
        )

        coEvery { requestBodyHelper.getLoginProperty("emilys","emilyspass") } returns dummyRequestJson
        coEvery { loginUseCase(dummyRequestJson) } returns flow {
            emit(ResponseState.Success(loginData))
        }

        // use default username and password and trigger ClickOnKeyboardDone event
        viewModel.onEvent(LoginUiEvent.ClickOnKeyboardDone)

        viewModel.loginUiState.test {

            //skip initial
            skipItems(1)

            val loading = awaitItem() as LoginUiState.Initial
            assertEquals(true, loading.isLoading)

            val apiSuccess = awaitItem()
            assertEquals(true, apiSuccess is LoginUiState.LoggedIn)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `click submit emit loading and then success`() = runTest {

        val loginData = LoginData(
            username = "username",
            email = "email@test.com",
            firstName = "firstName",
            lastName = "lastName",
            gender = "gender",
            image = "image"
        )

        coEvery { requestBodyHelper.getLoginProperty("username","password") } returns dummyRequestJson
        coEvery { loginUseCase(dummyRequestJson) } returns flow {
            emit(ResponseState.Success(loginData))
        }
        viewModel.onEvent(LoginUiEvent.UsernameChanged("username"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("password"))
        viewModel.onEvent(LoginUiEvent.ClickOnLogin)

        viewModel.loginUiState.test {

            //skip initial + UserNameChanged + PasswordChanged
            skipItems(3)

            val loading = awaitItem() as LoginUiState.Initial
            assertEquals(true, loading.isLoading)

            val apiSuccess = awaitItem()
            assertEquals(true, apiSuccess is LoginUiState.LoggedIn)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `click submit emit loading and then error`() = runTest {

        val error = ApiError.Unknown("Server Error")
        coEvery { requestBodyHelper.getLoginProperty("username","password") } returns dummyRequestJson
        coEvery { loginUseCase(dummyRequestJson) } returns flow {
            emit(ResponseState.Error(error))
        }
        viewModel.onEvent(LoginUiEvent.UsernameChanged("username"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("password"))


        viewModel.loginUiState.test {

            //skip initial + UserNameChanged + PasswordChanged
            skipItems(3)

            // Trigger ClickOnLogin event
            viewModel.onEvent(LoginUiEvent.ClickOnLogin)

            val loading = awaitItem() as LoginUiState.Initial
            assertEquals(true, loading.isLoading)

            val apiError = awaitItem() as LoginUiState.Initial
            assertEquals(false, apiError.isLoading)
            assertEquals(true, apiError.apiError != null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `DismissApiError clears apiError from state`() = runTest {

        val error = ApiError.Unknown("Server Error")
        coEvery { requestBodyHelper.getLoginProperty("username","password") } returns dummyRequestJson
        coEvery { loginUseCase(dummyRequestJson) } returns flow {
            emit(ResponseState.Error(error))
        }

        viewModel.onEvent(LoginUiEvent.UsernameChanged("username"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("password"))

        viewModel.loginUiState.test {

            //skip initial + UserNameChanged + PasswordChanged
            skipItems(3)

            // Trigger ClickOnLogin event
            viewModel.onEvent(LoginUiEvent.ClickOnLogin)

            //skip loading
            skipItems(1)

            val apiError = awaitItem() as LoginUiState.Initial
            assertEquals(false, apiError.isLoading)
            assertEquals(true, apiError.apiError != null)

            //Trigger DismissApiError event
            viewModel.onEvent(LoginUiEvent.DismissApiError)
            val cleared = awaitItem() as LoginUiState.Initial
            assertEquals(null, cleared.apiError)

            cancelAndIgnoreRemainingEvents()
        }
    }

}