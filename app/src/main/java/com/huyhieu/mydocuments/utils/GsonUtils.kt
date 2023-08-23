package com.huyhieu.mydocuments.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.huyhieu.mydocuments.libraries.utils.logError

inline fun <reified T> String?.toData(): T? {
    this ?: return null
    return try {
        Gson().fromJson(this, object : TypeToken<T>() {}.type)
    } catch (ex: Exception) {
        logError("GsonUtils - toMutableListData: ${ex.message}")
        null
    }
}

inline fun <reified T> String?.toMutableListData(): MutableList<T>? {
    this ?: return null
    return try {
        val type = object : TypeToken<MutableList<T>>() {}.type
        Gson().fromJson(this, type)
    } catch (ex: Exception) {
        logError("GsonUtils - toMutableListData: ${ex.message}")
        mutableListOf()
    }
}

fun Any?.toJson(): String {
    this ?: return ""
    return try {
        Gson().toJson(this) ?: ""
    } catch (ex: Exception) {
        logError("GsonUtils - toJson: ${ex.message}")
        ""
    }
}