package com.collatzinc.androidunittesting.di

import com.collatzinc.androidunittesting.data.network.TokenAuthenticatorMutex
import com.collatzinc.androidunittesting.data.network.AuthApiService
import com.collatzinc.androidunittesting.data.network.MainApiService
import com.collatzinc.androidunittesting.data.network.HeaderInterceptor
import com.collatzinc.androidunittesting.data.network.RetrofitHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(
        headerInterceptor: HeaderInterceptor,
        tokenAuthenticator: TokenAuthenticatorMutex
    ): OkHttpClient {
        return RetrofitHelper.getOkHttpClient()
            .addInterceptor(headerInterceptor)
            .authenticator(tokenAuthenticator)
            .build()

    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return RetrofitHelper.getRetrofitBuilder()
    }

    @Singleton
    @Provides
    fun providesAuthApiService(retrofit: Retrofit.Builder): AuthApiService {
        return retrofit
            .build()
            .create(AuthApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesMainApiService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): MainApiService {
        return retrofit
            .client(okHttpClient)
            .build()
            .create(MainApiService::class.java)
    }
}