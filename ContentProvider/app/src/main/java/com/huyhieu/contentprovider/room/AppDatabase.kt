package com.huyhieu.contentprovider.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huyhieu.contentprovider.room.dao.UserDao
import com.huyhieu.contentprovider.room.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}