package com.collatzinc.androidunittesting.common.validators

interface InputValidator {
    fun validate(input: String, isOptional: Boolean = false): ValidationResult
}