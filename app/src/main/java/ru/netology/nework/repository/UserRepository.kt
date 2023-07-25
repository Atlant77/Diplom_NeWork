package ru.netology.nework.repository

import ru.netology.nework.dto.User
import javax.inject.Singleton

@Singleton
interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: Long): User
}