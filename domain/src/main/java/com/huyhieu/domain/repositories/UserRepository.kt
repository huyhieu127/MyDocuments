package com.huyhieu.domain.repositories

import com.huyhieu.domain.enitities.User

interface UserRepository {
    suspend fun insertUser(user: User): Long?
    suspend fun getUserById(userId: Long): User?
}