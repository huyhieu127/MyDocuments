package com.huyhieu.mydocuments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun main() {
    val user1 = User("John")
    val user2 = User("John")

    coroutineScope {
        val a = async(Dispatchers.Main) {
            1
        }
        a.await()
    }
}

data class User(var name: String)