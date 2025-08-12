package com.collatzinc.androidunittesting.data.mapper

import com.collatzinc.androidunittesting.data.model.LoginDTO
import org.junit.Assert.assertEquals
import org.junit.Test


class LoginDTOToDomainMapperTest {
    private val mapper = LoginDTOToDomainMapper()

    @Test
    fun `map should correctly convert DTO to domain model`() {
        val dto = LoginDTO(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            id = 123,
            username = "john_doe",
            email = "john@example.com",
            firstName = "John",
            lastName = "Doe",
            gender = "Male",
            image = "profile.jpg"
        )

        val result = mapper.map(dto)

        assertEquals("john_doe", result.username)
        assertEquals("john@example.com", result.email)
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        assertEquals("Male", result.gender)
        assertEquals("profile.jpg", result.image)
    }
}