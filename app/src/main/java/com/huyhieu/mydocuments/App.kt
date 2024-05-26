package com.huyhieu.mydocuments

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.huyhieu.documentssdk.DocSdkInstance
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.repository.remote.NetworkUtils
import com.huyhieu.mydocuments.shared.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : SplitCompatApplication() {

    companion object {

        private var instance: App? = null
        fun getInstance(): App {
            return instance as App
        }

        val ins by lazy { getInstance() }

        var heightStatusBar = 0
    }

    /**
     * Use at file [../shared/App.kt]
     * */
    val fireStore by lazy { FirebaseStorage.getInstance() }
    val sharedPref by lazy { SharedPreferencesManager(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        NetworkUtils.requestNetwork()
        DocSdkInstance.init("KEY FOR DOCUMENTS SDK!")

        // Get FCM registration token
        getFCMToken()
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                logDebug("Fetching FCM registration token failed - ${task.exception}")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = "FCMToken: $token"
            logDebug(msg)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }
}