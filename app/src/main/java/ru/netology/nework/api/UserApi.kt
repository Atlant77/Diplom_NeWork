package ru.netology.nework.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nework.dto.PushToken
import ru.netology.nework.dto.User

interface UserApi {
//    Users

    //GET /api/users/ api_users_read
    @GET("users")
    suspend fun getAll(): Response<List<User>>

    //POST /api/users/authentication/ api_users_authentication_create
    @POST("users/authentication")
    suspend fun sendAuth(
        @Query("login") login: String,
        @Query("pass") pass: String
    ): Response<PushToken>

    //POST /api/users/registration/ api_users_registration_create
    @POST("/users/registration")
    suspend fun register(
        @Query("login") login: String,
        @Query("pass") pass: String,
        @Query("name") name: String
    ): Response<PushToken>

    @Multipart
    @POST("/users/registration")
    suspend fun register(
        @Part("login") login: RequestBody,
        @Part("pass") pass: RequestBody,
        @Part("name") name: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<PushToken>

    //GET /api/users/{user_id}/ api_users_read
    @GET("users/{user_id}")
    suspend fun getById(@Path("id") id: Long): Response<User>

    //TODO Надо разобраться
    @POST("users/push-tokens")
    suspend fun pushTokens(@Body pushToken: PushToken): Response<Unit>
}