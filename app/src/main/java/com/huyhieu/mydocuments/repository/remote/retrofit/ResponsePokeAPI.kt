package com.huyhieu.mydocuments.repository.remote.retrofit

data class ResponsePokeAPI<T>(
    var count: Int,
    var next: String?,
    var previous: String?,
    var results: T?,
)