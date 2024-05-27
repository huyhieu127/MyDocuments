package com.huyhieu.data.network

data class ResponsePokeAPI<T>(
    var count: Int,
    var next: String?,
    var previous: String?,
    var results: T?,
)