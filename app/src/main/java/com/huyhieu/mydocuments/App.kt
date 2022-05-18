package com.huyhieu.mydocuments

import android.net.ConnectivityManager
import androidx.multidex.MultiDexApplication
import com.huyhieu.mydocuments.repository.local.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

@HiltAndroidApp
class App : MultiDexApplication() {

    lateinit var flutterEngine : FlutterEngine

    companion object {

        private var instance: App? = null
        fun getInstance(): App {
            return instance as App
        }
    }

    val sharedPref by lazy { SharedPreferencesManager(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(this)

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put("my_engine_id", flutterEngine)
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