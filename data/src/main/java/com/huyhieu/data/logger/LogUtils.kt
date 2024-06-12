package com.huyhieu.data.logger

import android.util.Log

//Log
const val TAG_LOG = ">>>>>>>>>>>"

//TEMP BLOCK
private val arrMsgBlock = arrayOf("NetworkConfigs")

private fun checkBlockLogger(str: String?): Boolean? {
    arrMsgBlock.forEach {
        if (str?.contains(it) == true) return null
    }
    return true
}

fun logError(str: String? = "", tag: String? = TAG_LOG) {
    checkBlockLogger(str) ?: return
    try {
        Log.e(tag, "ERROR: $str")
    } catch (_: Exception) {

    }
}

fun logDebug(str: String? = "", tag: String? = TAG_LOG) {
    checkBlockLogger(str) ?: return
    try {
        Log.d(tag, "DEBUG: $str")
    } catch (_: Exception) {

    }
}

fun logInfo(str: String? = "", tag: String? = TAG_LOG) {
    checkBlockLogger(str) ?: return
    try {
        Log.i(tag, "INFO: $str")
    } catch (_: Exception) {

    }
}