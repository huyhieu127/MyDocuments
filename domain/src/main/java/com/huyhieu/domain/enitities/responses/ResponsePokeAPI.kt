package com.huyhieu.domain.enitities.responses

data class ResponsePokeAPI<T>(
    var count: Int,
    var next: String?,
    var previous: String?,
    var results: T?,
)