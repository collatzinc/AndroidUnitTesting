package com.collatzinc.androidunittesting.data.network

import com.collatzinc.androidunittesting.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    fun getRetrofitBuilder(): Retrofit.Builder {

        val gson = GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

    }

    fun getOkHttpClient(): OkHttpClient.Builder{
        val client = OkHttpClient.Builder().also { client ->
            client.readTimeout(60, TimeUnit.SECONDS)
            client.writeTimeout(60, TimeUnit.SECONDS)
            client.connectTimeout(60, TimeUnit.SECONDS)
        }

        return client
    }

}