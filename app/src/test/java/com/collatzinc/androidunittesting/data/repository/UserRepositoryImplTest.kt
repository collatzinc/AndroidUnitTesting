package com.collatzinc.androidunittesting.data.repository

import app.cash.turbine.test
import com.collatzinc.androidunittesting.data.mapper.UserDTOToDomainMapper
import com.collatzinc.androidunittesting.data.model.UserDTO
import com.collatzinc.androidunittesting.data.network.ApiError
import com.collatzinc.androidunittesting.data.network.ErrorMapper
import com.collatzinc.androidunittesting.data.network.MainApiService
import com.collatzinc.androidunittesting.data.network.ResponseState
import com.collatzinc.androidunittesting.domain.model.UserData
import com.google.gson.JsonObject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import java.io.IOException


class UserRepositoryImplTest {

    private lateinit var sut: UserRepositoryImpl
    private val mockService = mockk<MainApiService>()
    private val errorMapper = mockk<ErrorMapper>()
    private lateinit var userDTOToDomainMapper: UserDTOToDomainMapper
    private lateinit var requestParam: JsonObject

    @Before
    fun setUp() {
        userDTOToDomainMapper = UserDTOToDomainMapper()
        sut = UserRepositoryImpl(mockService, errorMapper, userDTOToDomainMapper)
        requestParam = JsonObject()
    }

    @Test
    fun `getDetails returns success with token`() = runTest {

        // Arrange

        val dto = UserDTO(
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
        val apiResponse = retrofit2.Response.success(dto)

        coEvery { mockService.userDetails() } returns apiResponse

        // Act
        val result = sut.getDetails()

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Success)
            assertEquals(domain, (item as ResponseState.Success).data)
            awaitComplete()
        }

    }

    @Test
    fun `getDetails returns network error on IOException`() = runTest {

        // Arrange
        coEvery { mockService.userDetails() } throws IOException()
        every { errorMapper.map(any<Throwable>()) } returns ApiError.Network

        // Act
        val result = sut.getDetails()

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Error)
            assertEquals(ApiError.Network, (item as ResponseState.Error).apiError)
            awaitComplete()
        }

    }

    @Test
    fun `getDetails returns Unknown on 400 error`() = runTest {

        // Arrange
        val response = retrofit2.Response.error<UserDTO>(
            400,
            byteArrayOf().toResponseBody()
        )
        coEvery { mockService.userDetails() } returns response
        every { errorMapper.map(400, any()) } returns ApiError.Unknown("Api error message")

        // Act
        val result = sut.getDetails()

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
    fun `getDetails returns Unauthorized on 401 error`() = runTest {

        // Arrange
        val response = retrofit2.Response.error<UserDTO>(
            401,
            byteArrayOf().toResponseBody()
        )
        coEvery { mockService.userDetails() } returns response
        every { errorMapper.map(401, any()) } returns ApiError.Unauthorized

        // Act
        val result = sut.getDetails()

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Error)
            assertEquals(ApiError.Unauthorized, (item as ResponseState.Error).apiError)
            awaitComplete()
        }

    }

    @Test
    fun `getDetails returns Forbidden on 403 error`() = runTest {

        // Arrange
        val response =
            retrofit2.Response.error<UserDTO>(
                403,
                byteArrayOf().toResponseBody()
            )
        coEvery { mockService.userDetails() } returns response
        every { errorMapper.map(403, any()) } returns ApiError.Forbidden

        // Act
        val result = sut.getDetails()

        // Assert
        result.test {
            val item = awaitItem()
            assertEquals(true, item is ResponseState.Error)
            assertEquals(ApiError.Forbidden, (item as ResponseState.Error).apiError)
            awaitComplete()
        }

    }

    @Test
    fun `getDetails returns Unknown on unexpected exception`() = runTest {

        // Arrange
        val exception = RuntimeException("Something went wrong")
        coEvery { mockService.userDetails() } throws exception
        every { errorMapper.map(exception) } returns ApiError.Unknown("Something went wrong")

        // Act
        val result = sut.getDetails()

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

}