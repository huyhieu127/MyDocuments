package com.huyhieu.mydocuments.base

data class LoadingState<out T>(
    var isLoading: Boolean = false,
    val data: T?,
    val apiKey: String = "",
    val tag: String = "",
)