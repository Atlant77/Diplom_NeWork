package ru.netology.nework.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nework.api.UserApi
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.Token
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.RegistrationError
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: UserApi,
) : AuthRepository {
    override suspend fun authentication(login: String, pass: String): Token {
        try {
            val response = apiService.authentication(login, pass)
            if (!response.isSuccessful) {
                when (response.code()) {
                    400 -> throw RegistrationError
                    else -> throw ApiError(response.code(), response.message())
                }
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }

    override suspend fun registration(login: String, pass: String, name: String): Token {
        try {
            val response = apiService.registration(login, pass, name)
            if (!response.isSuccessful) {
                when (response.code()) {
                    400 -> throw RegistrationError
                    else -> throw ApiError(response.code(), response.message())
                }
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }

    override suspend fun registrationWithPhoto(
        login: RequestBody,
        pass: RequestBody,
        name: RequestBody,
        avatar: MediaUpload
    ): Token {
        try {
            val file = MultipartBody.Part.createFormData(
                "file", avatar.file.name, avatar.file.asRequestBody()
            )

            val response = apiService.registrationWithPhoto(login, pass, name, file)
            if (!response.isSuccessful) {
                when (response.code()) {
                    400 -> throw RegistrationError
                    else -> throw ApiError(response.code(), response.message())
                }
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            println(e.message)
            throw NetworkError
        }
    }
}