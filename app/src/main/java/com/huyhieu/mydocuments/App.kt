package com.huyhieu.mydocuments

import android.net.ConnectivityManager
import androidx.multidex.MultiDexApplication
import com.huyhieu.mydocuments.repository.local.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : MultiDexApplication() {

    companion object {

        private var instance: App? = null
        fun getInstance(): App {
            return instance as App
        }
        val app by lazy { getInstance() }
    }

    val sharedPref by lazy { SharedPreferencesManager(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun checkNetwork(): Boolean {
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return run {
            val is3g = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                ?.isConnectedOrConnecting
            //For WiFi Check
            val isWifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                ?.isConnectedOrConnecting
            is3g ?: false || isWifi ?: false
        }
    }

}