package com.huyhieu.data.di.app

import android.content.Context
import com.huyhieu.data.room.AppRoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providerRoomDatabase(@ApplicationContext appContext: Context) =
        AppRoomDataBase.getInstance(appContext)

    @Singleton
    @Provides
    fun providerCalendarMemoDao(room: AppRoomDataBase) = room.userDao()
}