package com.huyhieu.mydocuments.hilt

import android.app.Application
import com.huyhieu.mydocuments.repository.local.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(app: Application) = AppDatabase.getInstance(app)
}