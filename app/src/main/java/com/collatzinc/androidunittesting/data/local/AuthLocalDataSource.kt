package com.collatzinc.androidunittesting.data.local

interface AuthLocalDataSource {
    fun setAccessToken(data: String?)
    fun getAccessToken(): String?
    fun setRefreshToken(data: String?)
    fun getRefreshToken(): String?
    fun clear()
}