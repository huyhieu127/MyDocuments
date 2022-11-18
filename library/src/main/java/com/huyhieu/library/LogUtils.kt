package com.huyhieu.mydocuments.utils

import android.util.Log

//Log
const val TAG_LOG = ">>>>>>>>>>>"

fun logError(str: String? = "") {
    try {
        Log.e(TAG_LOG, "ERROR: $str")
    } catch (_: Exception) {

    }
}

fun logDebug(str: String? = "") {
    try {
        Log.d(TAG_LOG, "DEBUG: $str")
    } catch (_: Exception) {

    }
}

fun logInfo(str: String? = "") {
    try {
        Log.i(TAG_LOG, "INFO: $str")
    } catch (_: Exception) {

    }
}