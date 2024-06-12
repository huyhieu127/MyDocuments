package com.huyhieu.data.network

enum class NetworkStatus(val status: Int) {
    OK(200),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    TIME_OUT(999)
}