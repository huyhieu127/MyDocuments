package com.huyhieu.mydocuments.utils

import com.huyhieu.mydocuments.libraries.utils.logError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
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

val coroutineDelayed = CoroutineScope(Dispatchers.Main)
fun delayed(duration: Long = 200L, onRunnable: () -> Unit) {
    coroutineDelayed.cancel()
    coroutineDelayed.launch {
        delay(duration)
        onRunnable.invoke()
    }
}