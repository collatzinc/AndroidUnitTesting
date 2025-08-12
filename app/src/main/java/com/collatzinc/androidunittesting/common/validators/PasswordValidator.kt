package com.collatzinc.androidunittesting.common.validators


import com.collatzinc.androidunittesting.R
import com.collatzinc.androidunittesting.common.Limits
import com.collatzinc.androidunittesting.common.Utils

class PasswordValidator : InputValidator {
    override fun validate(input: String, isOptional: Boolean): ValidationResult {

        return if (isOptional.not() && input.isBlank()) {
            //error
            ValidationResult(R.string.please_enter_password)
        } else if (input.isNotBlank() && input.length < Limits.PASSWORD.value) {
            //error
            ValidationResult(R.string.password_minimum_characters_validation)
        } else if (input.isNotBlank() && Utils.passwordWithoutSpaceAtStartAndEnd()
                .matcher(input)
                .matches().not()
        ) {
            ValidationResult(R.string.your_password_cannot_start_or_end_with_a_blank_space)
        } else {
            //validated
            ValidationResult()
        }
    }
}