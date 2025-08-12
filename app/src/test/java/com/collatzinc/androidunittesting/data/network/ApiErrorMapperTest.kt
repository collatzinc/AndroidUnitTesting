package com.collatzinc.androidunittesting.data.network

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class ApiErrorMapperTest {
    private val httpException = mockk<HttpException>()
    private lateinit var apiErrorMapper: ApiErrorMapper

    @Before
    fun setUp() {
        apiErrorMapper = ApiErrorMapper()

    }

    @Test
    fun `map throwable - IOException returns Network error`() {
        val error = IOException()
        val result = apiErrorMapper.map(error)
        assertEquals(true,result is ApiError.Network)
    }

    @Test
    fun `map throwable - HttpException 400 returns Unknown`(){
        every { httpException.code() } returns 400
        every { httpException.message() } returns "Error Message"
        val result = apiErrorMapper.map(httpException)
        assertEquals(true,result is ApiError.Unknown)
        assertEquals("Error Message",(result as ApiError.Unknown).message)
    }

    @Test
    fun `map throwable - HttpException 401 returns Unauthorized`() {
        every { httpException.code() } returns 401
        every { httpException.message() } returns "Unauthorized"
        val result = apiErrorMapper.map(httpException)
        assertEquals(true,result is ApiError.Unauthorized)
    }

    @Test
    fun `map throwable - NullPointerException returns Unknown`(){
        val error = NullPointerException("Error Message")
        val result = apiErrorMapper.map(error)
        assertEquals(true,result is ApiError.Unknown)
    }

    @Test
    fun `map throwable - Other Exceptions returns Unknown`(){
        val error = RuntimeException("Error Message")
        val result = apiErrorMapper.map(error)
        assertEquals(true,result is ApiError.Unknown)
    }

    @Test
    fun `map errorCode 403 and returns Forbidden`(){
        val json = """
            {
                "message": "Forbidden"
            }
        """.trimIndent()
        val responseBody = json.toResponseBody("application/json".toMediaTypeOrNull())
        val result = apiErrorMapper.map(403, responseBody)

        assertEquals(true, result is ApiError.Forbidden)

    }

    @Test
    fun `map errorCode 400 and returns Unknown with message`(){
        val json = """
            {
                 "message": "Error message"
            }
        """.trimIndent()
        val responseBody = json.toResponseBody("application/json".toMediaTypeOrNull())
        val result = apiErrorMapper.map(400, responseBody)


        assertEquals(true, result is ApiError.Unknown)
        assertEquals("Error message", (result as ApiError.Unknown).message)

    }

    @Test
    fun `map errorCode 401 and returns Unauthorized with message`(){
        val json = """
            {
                  "message": "Token expired"
            }
        """.trimIndent()
        val responseBody = json.toResponseBody("application/json".toMediaTypeOrNull())
        val result = apiErrorMapper.map(401, responseBody)


        assertEquals(true, result is ApiError.Unauthorized)

    }

}