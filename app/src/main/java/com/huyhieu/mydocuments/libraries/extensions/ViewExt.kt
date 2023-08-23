package com.huyhieu.mydocuments.libraries.extensions

import android.view.View

internal var time = 0L
fun View.setOnClickMyListener(delay: Long = 500L, onClick: () -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
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