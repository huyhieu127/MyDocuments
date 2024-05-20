package com.huyhieu.contentprovider

import android.app.Application
import androidx.room.Room
import com.huyhieu.contentprovider.room.AppDatabase

class App : Application() {

    lateinit var db: AppDatabase
    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "AppContentProviderDB"
        ).build()
    }

    companion object {
        private var instance: App? = null
        val ins get() = instance as App
    }
}