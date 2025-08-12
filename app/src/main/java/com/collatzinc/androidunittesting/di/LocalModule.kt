package com.collatzinc.androidunittesting.di

import com.collatzinc.androidunittesting.data.local.AuthLocalDataSource
import com.collatzinc.androidunittesting.data.local.LocalData
import com.collatzinc.androidunittesting.data.network.ApiErrorMapper
import com.collatzinc.androidunittesting.data.network.ErrorMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class LocalModule {

    @Binds
    @Singleton
    abstract fun providesLocalData(authLocalDataSource: AuthLocalDataSource): LocalData
}