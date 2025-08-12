package com.collatzinc.androidunittesting.domain.mapper

import com.collatzinc.androidunittesting.domain.model.UserData
import org.junit.Assert.assertEquals
import org.junit.Test


class UserDataToUiMapperTest {

    private val mapper = UserDataToUiMapper()

    @Test
    fun `map should correctly format fields`() {
        // Arrange
        val userData = UserData(
            firstName = "Jane",
            lastName = "Smith",
            maidenName = null,
            age = 28,
            gender = "Female",
            email = "jane@example.com",
            phone = "1234567890",
            username = "janesmith",
            birthDate = "1995-05-15",
            image = "avatar.png",
            bloodGroup = "O+"
        )

        // Act
        val uiData = mapper.map(userData)

        // Assert
        assertEquals("Jane Smith", uiData.fullName)
        assertEquals(28, uiData.age)
        assertEquals("Female", uiData.gender)
        assertEquals("jane@example.com", uiData.email)
        assertEquals("1234567890", uiData.phone)
        assertEquals("@janesmith", uiData.username)
        assertEquals("1995-05-15", uiData.birthDate)
        assertEquals("avatar.png", uiData.image)
        assertEquals("O+", uiData.bloodGroup)
    }

}