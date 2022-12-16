package com.huyhieu.library.extensions

import android.view.View
import com.huyhieu.library.utils.logDebug

internal var time = 0L
fun View.setOnClickMyListener(delay: Long = 500L, onClick: () -> Unit) {
    logDebug("$time")
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        logDebug("setOnClickListener $time")
        if (currentTime - time > delay) {
            time = currentTime
            //logDebug("setOnClickMyListener: ${this.getNameById(id)}")
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