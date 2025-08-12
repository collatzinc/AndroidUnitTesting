package com.collatzinc.androidunittesting.domain.mapper

import com.collatzinc.androidunittesting.data.mapper.Mapper
import com.collatzinc.androidunittesting.data.model.UserDTO
import com.collatzinc.androidunittesting.domain.model.UserData
import com.collatzinc.androidunittesting.presentation.screen.profile.UserUiData
import javax.inject.Inject

class UserDataToUiMapper @Inject constructor() :
    Mapper<UserData, UserUiData> {
    override fun map(from: UserData): UserUiData {

        return UserUiData(
            fullName = "${from.firstName ?: ""} ${from.lastName ?: ""}",
            age = from.age,
            gender = from.gender,
            email = from.email,
            phone = from.phone,
            username = "@${from.username ?: ""}",
            birthDate = from.birthDate,
            image = from.image,
            bloodGroup = from.bloodGroup


        )
    }
}