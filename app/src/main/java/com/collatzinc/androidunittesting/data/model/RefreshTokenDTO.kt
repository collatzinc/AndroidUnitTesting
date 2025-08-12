package com.collatzinc.androidunittesting.data.model

import com.google.gson.annotations.SerializedName

data class RefreshTokenDTO(
    @SerializedName("accessToken") var accessToken: String? = null,
    @SerializedName("refreshToken") var refreshToken: String? = null,
)