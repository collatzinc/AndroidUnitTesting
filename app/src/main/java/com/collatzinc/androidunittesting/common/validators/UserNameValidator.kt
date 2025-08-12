package com.collatzinc.androidunittesting.common.validators

import com.collatzinc.androidunittesting.R


class UserNameValidator() : InputValidator {
    override fun validate(input: String, isOptional: Boolean): ValidationResult {
        return if (isOptional.not() && input.isBlank()) {
            //error
            ValidationResult(R.string.user_is_required_for_login)
        } else {
            //validated
            ValidationResult()
        }
    }
}