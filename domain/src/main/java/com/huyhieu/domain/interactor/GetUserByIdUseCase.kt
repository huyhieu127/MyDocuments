package com.huyhieu.domain.interactor

import com.huyhieu.domain.enitities.User
import com.huyhieu.domain.repositories.UserRepository

class GetUserByIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Long): User? {
        return userRepository.getUserById(userId)
    }
}