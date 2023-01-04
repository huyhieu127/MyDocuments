package com.huyhieu.mydocuments

import androidx.multidex.MultiDexApplication
import com.google.firebase.storage.FirebaseStorage
import com.huyhieu.mydocuments.repository.remote.NetworkUtils
import com.huyhieu.mydocuments.shared.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : MultiDexApplication() {

    companion object {

        private var instance: App? = null
        fun getInstance(): App {
            return instance as App
        }

        val ins by lazy { getInstance() }

        var heightStatusBar = 0
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        NetworkUtils.requestNetwork()
    }

    /**
     * Use at file [../shared/App.kt]
     * */
    val fireStore by lazy { FirebaseStorage.getInstance() }
    val sharedPref by lazy { SharedPreferencesManager(this) }
}