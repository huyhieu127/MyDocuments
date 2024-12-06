package com.huyhieu.mydocuments.utils

import com.huyhieu.data.logger.logError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

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

//Updating....
fun delayed(duration: Long = 100L, onRunnable: () -> Unit) =
    CoroutineScope(Dispatchers.Main).launch {
        delay(duration)
        if (isActive) {
            onRunnable.invoke()
        }
    }


fun CoroutineScope.delayed(duration: Long = 100L, onRunnable: () -> Unit) = launch {
    delay(duration)
    if (isActive) {
        onRunnable.invoke()
    }
}

fun safeDelay(scope: CoroutineScope, duration: Long = 100L, onRunnable: () -> Unit) {
    val weakRunnable = WeakReference(onRunnable)
    scope.launch {
        delay(duration)
        if (isActive) {
            weakRunnable.get()?.invoke()
        }
    }
}