package com.huyhieu.data.repositories

import com.huyhieu.data.room.dao.UserDao
import com.huyhieu.data.room.entities.UserEntity
import com.huyhieu.domain.enitities.User
import com.huyhieu.domain.repositories.UserRepository

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: User): Long? {
        return userDao.insertUser(user.toEntity())
    }

    override suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)?.toUser()
    }
}

fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        username = this.username,
        email = this.email
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        username = this.username,
        email = this.email
    )
}