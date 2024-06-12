package com.huyhieu.data.di.repositories

import com.huyhieu.data.repositories.UserRepositoryImpl
import com.huyhieu.data.room.dao.UserDao
import com.huyhieu.domain.interactor.GetUserByIdUseCase
import com.huyhieu.domain.interactor.InsertUserUseCase
import com.huyhieu.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    fun provideGetUserByIdUseCase(userRepository: UserRepository): GetUserByIdUseCase {
        return GetUserByIdUseCase(userRepository)
    }

    @Provides
    fun provideInsertUserUseCase(userRepository: UserRepository): InsertUserUseCase {
        return InsertUserUseCase(userRepository)
    }
}