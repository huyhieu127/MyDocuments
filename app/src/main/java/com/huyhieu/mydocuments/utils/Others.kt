package com.huyhieu.mydocuments.utils

import com.huyhieu.data.logger.logError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun tryCatch(onTry: () -> Unit) {
    try {
        onTry()
    } catch (ex: Exception) {
        logError("tryCatch: $ex")
    }
}

fun tryCatch(onTry: () -> Unit, onCatch: (Exception) -> Unit) {
    try {
        onTry()
    } catch (ex: Exception) {
        logError("tryCatch: $ex")
        onCatch(ex)
    }
}

var jobDelayed: Job = Job()
fun delayed(duration: Long = 100L, onRunnable: () -> Unit) {
    jobDelayed.cancel()
    jobDelayed = CoroutineScope(Dispatchers.Main).launch {
        delay(duration)
        if (isActive) {
            onRunnable.invoke()
        }
    }
}