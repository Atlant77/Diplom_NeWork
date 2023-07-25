package ru.netology.nework.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nework.dto.Token
import ru.netology.nework.dto.User

interface UserApi {
//    Users

    //GET /api/users/ api_users_read
    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    //POST /api/users/authentication/ api_users_authentication_create
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun authentication(
        @Field("login") login: String,
        @Field("password") password: String
    ): Response<Token>

    //POST /api/users/registration/ api_users_registration_create
    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registration(
        @Query("login") login: String,
        @Query("password") password: String,
        @Query("name") name: String
    ): Response<Token>

    @Multipart
    @POST("users/registration")
    suspend fun registrationWithPhoto(
        @Part("login") login: RequestBody,
        @Part("password") pass: RequestBody,
        @Part("name") name: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<Token>

    //GET /api/users/{user_id}/ api_users_read
    @GET("users/{user_id}")
    suspend fun getUserById(@Path("id") id: Long): Response<User>
}