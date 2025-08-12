package com.collatzinc.androidunittesting.data.mapper

import com.collatzinc.androidunittesting.data.model.UserDTO
import org.junit.Assert.assertEquals
import org.junit.Test


class UserDTOToDomainMapperTest {

    private val mapper = UserDTOToDomainMapper()

    @Test
    fun `map should correctly convert UserDTO to UserData`() {
        val dto = UserDTO(
            firstName = "Jane",
            lastName = "Smith",
            maidenName = "Doe",
            age = 30,
            gender = "Female",
            email = "jane@example.com",
            phone = "1234567890",
            username = "janesmith",
            birthDate = "1995-05-15",
            image = "avatar.png",
            bloodGroup = "O+"
        )

        val result = mapper.map(dto)

        assertEquals("Jane", result.firstName)
        assertEquals("Smith", result.lastName)
        assertEquals("Doe", result.maidenName)
        assertEquals(30, result.age)
        assertEquals("Female", result.gender)
        assertEquals("jane@example.com", result.email)
        assertEquals("1234567890", result.phone)
        assertEquals("janesmith", result.username)
        assertEquals("1995-05-15", result.birthDate)
        assertEquals("avatar.png", result.image)
        assertEquals("O+", result.bloodGroup)
    }

}