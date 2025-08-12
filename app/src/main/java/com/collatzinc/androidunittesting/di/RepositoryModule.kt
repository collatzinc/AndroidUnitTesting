package com.collatzinc.androidunittesting.di

import com.collatzinc.androidunittesting.data.repository.AuthRepositoryImpl
import com.collatzinc.androidunittesting.data.repository.UserRepositoryImpl
import com.collatzinc.androidunittesting.domain.repository.AuthRepository
import com.collatzinc.androidunittesting.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository


}