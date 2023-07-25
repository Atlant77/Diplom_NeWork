package ru.netology.nework.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.netology.nework.api.UserApi
import ru.netology.nework.dto.User
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApi,
    @ApplicationContext
    private val context: Context,
) : UserRepository {
    override suspend fun getAllUsers(): List<User> {
        try {
            val response = apiService.getAllUsers()
            return response.body() ?: throw UnknownError
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getUserById(id: Long): User {
        try {
            val response = apiService.getUserById(id)
            return response.body() ?: throw UnknownError
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}