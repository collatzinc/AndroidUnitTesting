package com.collatzinc.androidunittesting.data.mapper

import com.collatzinc.androidunittesting.data.model.LoginDTO
import com.collatzinc.androidunittesting.domain.model.LoginData
import javax.inject.Inject

class LoginDTOToDomainMapper @Inject constructor() :
    Mapper<LoginDTO, LoginData> {
    override fun map(from: LoginDTO): LoginData {

        return LoginData(
            username = from.username,
            email = from.email,
            firstName = from.firstName,
            lastName = from.lastName,
            gender = from.gender,
            image = from.image
        )
    }
}