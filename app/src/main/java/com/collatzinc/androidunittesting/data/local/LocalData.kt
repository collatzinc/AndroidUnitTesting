package com.collatzinc.androidunittesting.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalData @Inject constructor(@ApplicationContext private val context: Context):
    AuthLocalDataSource {

    companion object{
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        private const val LOGIN_SHARED_FILE = "LOGIN_FILE"
    }
    val userdata: SharedPreferences? = context.getSharedPreferences(LOGIN_SHARED_FILE, Context.MODE_PRIVATE)

    override fun setAccessToken(data: String?) {
        userdata?.edit {
            this.putString(ACCESS_TOKEN, data)
        }

    }
    override fun getAccessToken(): String? {
        return userdata?.getString(ACCESS_TOKEN,null)
    }

    override fun setRefreshToken(data: String?) {
        userdata?.edit {
            this.putString(REFRESH_TOKEN, data)
        }

    }
    override fun getRefreshToken(): String? {
        return userdata?.getString(REFRESH_TOKEN,null)
    }

    override fun clear() {
        userdata?.edit{
            clear()
        }
    }

}