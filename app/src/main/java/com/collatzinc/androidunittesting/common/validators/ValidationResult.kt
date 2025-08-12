package com.collatzinc.androidunittesting.common.validators

import androidx.annotation.StringRes

data class ValidationResult(
    @StringRes val errorMessage: Int? = null
) {
    val isValid: Boolean
        get() = errorMessage == null
}