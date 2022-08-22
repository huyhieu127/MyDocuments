package com.huyhieu.mydocuments.utils

import android.util.Log

//Log
const val TAG_LOG = ">>>>>>>>>>>"

fun logError(str: String? = "") {
    try {
        Log.e(TAG_LOG, "MSG: $str")
    }catch (ex: Exception){

    }
}

fun logDebug(str: String? = "") {
    try {
        Log.d(TAG_LOG, "MSG: $str")
    }catch (ex: Exception){

    }
}

fun logInfo(str: String? = "") {
    try {
        Log.i(TAG_LOG, "MSG: $str")
    }catch (ex: Exception){

    }
}