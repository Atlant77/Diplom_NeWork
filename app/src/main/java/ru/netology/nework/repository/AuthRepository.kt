package ru.netology.nework.repository

import okhttp3.RequestBody
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.Token

interface AuthRepository {
    suspend fun authentication(
        login: String,
        pass: String,
    ): Token

    suspend fun registration(
        login: String,
        pass: String,
        name: String,
    ): Token

    suspend fun registrationWithPhoto(
        login: RequestBody,
        pass: RequestBody,
        name: RequestBody,
        avatar: MediaUpload
    ): Token
}