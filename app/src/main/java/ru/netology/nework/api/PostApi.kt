package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nework.dto.Post

interface PostApi {
//    Posts

    //GET /api/posts/ api_posts_read
    @GET("posts")
    suspend fun getAllPosts(): Response<List<Post>>

    //POST /api/posts/ api_posts_create
    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    //GET /api/posts/latest/ api_posts_latest_read
    @GET("post/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Post>>

    //GET /api/posts/{post_id}/ api_posts_read
    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Unit>

    //DELETE /api/posts/{post_id}/ api_posts_delete
    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    //GET /api/posts/{post_id}/after/ api_posts_after_read
    @GET("posts/{id}/after")
    suspend fun getAfter(@Path("id") id: Long, @Query("count") count: Int): Response<List<Post>>

    // GET /api/posts/{post_id}/before/ api_posts_before_read
    @GET("posts/{id}/before")
    suspend fun getBefore(@Path("id") id: Long, @Query("count") count: Int): Response<List<Post>>

    //POST /api/posts/{post_id}/likes/ api_posts_likes_create
    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    //DELETE /api/posts/{post_id}/likes/ api_posts_likes_delete
    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<Post>

    //GET /api/posts/{post_id}/newer/ api_posts_newer_read
    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>
}