package com.collatzinc.androidunittesting.common.validators

import com.collatzinc.androidunittesting.R


class EmailValidator(
    private val emailRegex: Regex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    )
) : InputValidator {
    override fun validate(input: String, isOptional: Boolean): ValidationResult {
        return if (isOptional.not() && input.isBlank()) {
            //error
            ValidationResult(R.string.email_is_required_for_login)
        } else if (input.isNotBlank() && emailRegex.matches(input).not()) {
            //error
            ValidationResult(R.string.please_enter_valid_email)
        } else {
            //validated
            ValidationResult()
        }
    }
}