package com.huyhieu.documentssdk

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log


object DocSdkInstance {
    var key: String = ""
    var onData: ((data: String) -> Unit)? = null
    val packageAction = "com.huyhieu.DATA_ACTION"
    private var receiver: BroadcastReceiver? = null

    fun init(key: String) {
        if (key.isEmpty()) {
            Log.e(TAG, "Key is empty!")
            return
        }
        this.key = key
    }

    fun openSdk(context: Context) {
        Intent(context, DocSdkActivity::class.java).also {
            context.startActivity(it)
        }
    }

    fun subscribeData(activity: Activity, onData: (data: String) -> Unit) {
        // Trong ứng dụng
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val data = intent.getStringExtra("data").orEmpty()
                // Xử lý dữ liệu nhận được từ SDK
                onData(data)
            }
        }
        val filter = IntentFilter(packageAction)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            activity.registerReceiver(receiver, filter)
        }
    }

    fun unsubscribeData(activity: Activity) {
        if (receiver != null) {
            activity.unregisterReceiver(receiver)
            receiver = null
        }
    }
}