package com.huyhieu.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.huyhieu.data.room.dao.UserDao
import com.huyhieu.data.room.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppRoomDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppRoomDataBase? = null

        fun getInstance(context: Context): AppRoomDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppRoomDataBase {
            return Room.databaseBuilder(context, AppRoomDataBase::class.java, "MyDatabase")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}