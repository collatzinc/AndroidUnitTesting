package com.collatzinc.androidunittesting.data.repository

import app.cash.turbine.test
import com.collatzinc.androidunittesting.data.local.AuthLocalDataSource
import com.collatzinc.androidunittesting.data.local.LocalData
import com.collatzinc.androidunittesting.data.mapper.LoginDTOToDomainMapper
import com.collatzinc.androidunittesting.data.model.LoginDTO
import com.collatzinc.androidunittesting.data.network.ApiError
import com.collatzinc.androidunittesting.data.network.AuthApiService
import com.collatzinc.androidunittesting.data.network.ErrorMapper
import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.model.LoginData
import com.google.gson.JsonObject
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import java.io.IOException


class AuthRepositoryImplTest {

    private lateinit var sut: AuthRepositoryImpl
    private val mockService = mockk<AuthApiService>()
    private val localDataSource = mockk<LocalData>(relaxed = true)
    private val errorMapper = mockk<ErrorMapper>()
    private lateinit var loginDTOToDomainMapper: LoginDTOToDomainMapper
    private lateinit var requestParam: JsonObject

    @Before
    fun setUp() {
        loginDTOToDomainMapper = LoginDTOToDomainMapper()
        sut = AuthRepositoryImpl(mockService, localDataSource, errorMapper, loginDTOToDomainMapper)
        requestParam = JsonObject()
    }

    @Test
    fun `login returns success with token`() = runTest {

        // Arrange

        val dto = LoginDTO(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            id = 123,
            username = "username",
            email = "email@test.com",
            firstName = "firstName",
            lastName = "lastName",
            gender = "gender",
            image = "image"
        )
        val loginData = LoginData(
            username = "username",
            email = "email@test.com",
            firstName = "firstName",
            lastName = "lastName",
            gender = "gender",
            image = "image"
        )
        val apiResponse = retrofit2.Response.success(dto)

        coEvery { mockService.login(requestParam) } returns apiResponse
        every { localDataSource.setAccessToken("access_token") } just runs
        every { localDataSource.setRefreshToken("refresh_token") } just runs

        // Act
        val result = sut.login(requestParam)

        // Assert
        result.test {
            val item = awaitItem()
            verify(exactly = 1) { localDataSource.setAccessToken("access_token") }
            verify(exactly = 1) { localDataSource.setRefreshToken("refresh_token") }
            assertEquals(true, item is ResponseState.Success)
            assertEquals(loginData, (item as ResponseState.Success).data)
            awaitComplete()
        }

    }

    @Test
    fun `login returns network error on IOException`() = runTest {

        // Arrange
        coEvery { mockService.login(requestParam) } throws IOException()
        every { errorMapper.map(any<Throwable>()) } returns ApiError.Network

        // Act
        val result = sut.login(requestParam)

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Error)
            assertEquals(ApiError.Network, (item as ResponseState.Error).apiError)
            awaitComplete()
        }

    }

    @Test
    fun `login returns Unknown on 400 error`() = runTest {

        // Arrange
        val response = retrofit2.Response.error<LoginDTO>(
            400,
            byteArrayOf().toResponseBody()
        )
        coEvery { mockService.login(requestParam) } returns response
        every { errorMapper.map(400, any()) } returns ApiError.Unknown("Api error message")

        // Act
        val result = sut.login(requestParam)

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Error)
            assertEquals(
                ApiError.Unknown("Api error message"),
                (item as ResponseState.Error).apiError
            )
            awaitComplete()
        }

    }

    @Test
    fun `login returns Unauthorized on 401 error`() = runTest {

        // Arrange
        val response = retrofit2.Response.error<LoginDTO>(
            401,
            byteArrayOf().toResponseBody()
        )
        coEvery { mockService.login(requestParam) } returns response
        every { errorMapper.map(401, any()) } returns ApiError.Unauthorized

        // Act
        val result = sut.login(requestParam)

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Error)
            assertEquals(ApiError.Unauthorized, (item as ResponseState.Error).apiError)
            awaitComplete()
        }

    }

    @Test
    fun `login returns Forbidden on 403 error`() = runTest {

        // Arrange
        val response =
            retrofit2.Response.error<LoginDTO>(
                403,
                byteArrayOf().toResponseBody()
            )
        coEvery { mockService.login(requestParam) } returns response
        every { errorMapper.map(403, any()) } returns ApiError.Forbidden

        // Act
        val result = sut.login(requestParam)

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Error)
            assertEquals(ApiError.Forbidden, (item as ResponseState.Error).apiError)
            awaitComplete()
        }

    }

    @Test
    fun `login returns Unknown on unexpected exception`() = runTest {

        // Arrange
        val exception = RuntimeException("Something went wrong")
        coEvery { mockService.login(requestParam) } throws exception
        every { errorMapper.map(exception) } returns ApiError.Unknown("Something went wrong")

        // Act
        val result = sut.login(requestParam)

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Error)
            assertEquals(
                ApiError.Unknown("Something went wrong"),
                (item as ResponseState.Error).apiError
            )
            awaitComplete()
        }

    }

    @Test
    fun `getToken should return access token from localDataSource`() = runTest {
        // Arrange
        every { localDataSource.getAccessToken() } returns "sample_token"

        // Act
        val result = sut.getToken()

        // Assert
        assertEquals("sample_token", result)
        verify(exactly = 1) { localDataSource.getAccessToken() }
    }

    @Test
    fun `clearToken should call clear on localDataSource`() = runTest {
        // Arrange
        every { localDataSource.clear() } just runs

        // Act
        sut.clearToken()

        // Assert
        verify(exactly = 1) { localDataSource.clear() }
    }

}