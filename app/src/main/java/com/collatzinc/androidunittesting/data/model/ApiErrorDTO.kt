package com.collatzinc.androidunittesting.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    @SerialName("message")
    val message: String? = null
)