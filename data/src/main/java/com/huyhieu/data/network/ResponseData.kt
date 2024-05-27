package com.huyhieu.data.network

data class ResponseData<T>(
    var timestamp: Long,
    var code: Int,
    var message: String,
    var data: T?,
    var error: String,
)