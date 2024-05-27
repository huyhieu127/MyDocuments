package com.huyhieu.domain.interactor

import com.huyhieu.domain.enitities.User
import com.huyhieu.domain.repositories.UserRepository

class InsertUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): Long? {
        return userRepository.insertUser(user)
    }
}