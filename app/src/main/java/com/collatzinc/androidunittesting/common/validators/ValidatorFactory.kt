package com.collatzinc.androidunittesting.common.validators

import android.util.Patterns
import javax.inject.Inject

class ValidatorFactory @Inject constructor() {

    private val validators: Map<ValidatorParam, InputValidator> = mapOf(
        ValidatorParam.EMAIL to EmailValidator(Patterns.EMAIL_ADDRESS.toRegex()),
        ValidatorParam.USERNAME to UserNameValidator(),
        ValidatorParam.PASSWORD to PasswordValidator(),

        )

    fun get(param: ValidatorParam): InputValidator {
        return validators[param]
            ?: throw IllegalArgumentException("Incorrect parameter given,validator not found")
    }
}

enum class ValidatorParam {
    EMAIL,
    USERNAME,
    PASSWORD
}