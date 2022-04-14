package com.huyhieu.mydocuments.utils

import android.util.Log

//Log
const val TAG_LOG = ">>>>>>>>>>>"

fun logError(str: String? = "") {
    Log.e(TAG_LOG, "MSG: $str")
}

fun logDebug(str: String? = "") {
    Log.d(TAG_LOG, "MSG: $str")
}