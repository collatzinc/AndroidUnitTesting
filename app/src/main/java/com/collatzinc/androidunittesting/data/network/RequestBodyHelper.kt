package com.collatzinc.androidunittesting.data.network

import com.google.gson.JsonObject
import javax.inject.Inject

class RequestBodyHelper @Inject constructor() {
    companion object {
        private const val TAG = "RequestBody"
    }
    fun getLoginProperty(username:String?,password: String?): JsonObject {
        val propertyType = JsonObject()
//        propertyType.addProperty("username", "emilys")
        propertyType.addProperty("username", username)
        propertyType.addProperty("password", password)
//        propertyType.addProperty("password", "emilyspass")
        propertyType.addProperty("expiresInMins", 15)

        return propertyType
    }

    fun getRefreshTokenProperty(token: String): JsonObject {
        val propertyType = JsonObject()
        propertyType.addProperty("refreshToken", token)
        propertyType.addProperty("expiresInMins", 1)

        return propertyType
    }


}