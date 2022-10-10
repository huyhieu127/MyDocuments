package com.huyhieu.mydocuments.utils.extensions

import android.view.View
import com.huyhieu.mydocuments.utils.logDebug

fun View.setOnClickMyListener(delay: Long = 1000L, onClick: () -> Unit) {
    var time = System.currentTimeMillis()
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - time > delay) {
            time = currentTime
            logDebug("setOnClickMyListener: ${this.getNameById(id)}")
            onClick()
        }
    }
}

fun View.getNameById(id: Int): String {
    return try {
        this.resources.getResourceEntryName(this.id)
    } catch (ex: Exception) {
        ""
    }
}