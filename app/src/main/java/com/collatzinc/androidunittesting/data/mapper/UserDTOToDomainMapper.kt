package com.collatzinc.androidunittesting.data.mapper

import com.collatzinc.androidunittesting.data.model.UserDTO
import com.collatzinc.androidunittesting.domain.model.UserData
import javax.inject.Inject

class UserDTOToDomainMapper @Inject constructor() :
    Mapper<UserDTO, UserData> {
    override fun map(from: UserDTO): UserData {

        return UserData(
            firstName = from.firstName,
            lastName = from.lastName,
            maidenName = from.maidenName,
            age = from.age,
            gender = from.gender,
            email = from.email,
            phone = from.phone,
            username = from.username,
            birthDate = from.birthDate,
            image = from.image,
            bloodGroup = from.bloodGroup


        )
    }
}