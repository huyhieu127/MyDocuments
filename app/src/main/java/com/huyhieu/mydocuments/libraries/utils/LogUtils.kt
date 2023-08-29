package com.huyhieu.mydocuments.libraries.utils

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

fun logError(str: String? = "") {
    checkBlockLogger(str) ?: return
    try {
        Log.e(TAG_LOG, "ERROR: $str")
    } catch (_: Exception) {

    }
}

fun logDebug(str: String? = "") {
    checkBlockLogger(str) ?: return
    try {
        Log.d(TAG_LOG, "DEBUG: $str")
    } catch (_: Exception) {

    }
}

fun logInfo(str: String? = "") {
    checkBlockLogger(str) ?: return
    try {
        Log.i(TAG_LOG, "INFO: $str")
    } catch (_: Exception) {

    }
}