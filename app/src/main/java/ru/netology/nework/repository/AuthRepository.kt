package ru.netology.nework.repository

import ru.netology.nework.auth.AuthState
import ru.netology.nework.dto.Media
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.Token

interface AuthRepository {
    suspend fun logIn(
        login: String,
        pass: String,
    ): Token

    suspend fun register(
        login: String,
        pass: String,
        name: String,
    ): Token

    suspend fun registerWithPhoto(
        login: String,
        pass: String,
        name: String,
        avatar: MediaUpload
    ): Token
}