package ru.netology.nework.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.Media
import ru.netology.nework.dto.Post

interface EventApi {
    //    Events

    //GET /api/events/ api_events_read
    @GET("events")
    suspend fun getAllEvents(): Response<List<Event>>

    //POST /api/events/ api_events_create
    @POST("events")
    suspend fun saveEvent(@Body event: Event): Response<Event>

    //GET /api/events/latest/ api_events_latest_read
    @GET("events/latest")
    suspend fun getEventsLatest(@Query("count") count: Int): Response<List<Event>>

    //GET /api/events/{event_id}/ api_events_read
    @GET("events/{event_id}")
    suspend fun getEventById(@Path("event_id") id: Long): Response<Unit>

    //DELETE /api/events/{event_id}/ api_events_delete
    @DELETE("events/{event_id}")
    suspend fun removeEventById(@Path("event_id") id: Long): Response<Unit>

    //GET /api/events/{event_id}/after/ api_events_after_read
    @GET("events/{event_id}/after")
    suspend fun getEventsAfter(
        @Path("event_id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    //GET /api/events/{event_id}/before/ api_events_before_read
    @GET("events/{event_id}/before")
    suspend fun getEventsBefore(
        @Path("event_id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    //POST /api/events/{event_id}/likes/ api_events_likes_create
    @POST("events/{event_id}/likes")
    suspend fun likeEventById(@Path("event_id") id: Long): Response<Event>

    //DELETE /api/events/{event_id}/likes/ api_events_likes_delete
    @DELETE("events/{event_id}/likes")
    suspend fun dislikeEventById(@Path("event_id") id: Long): Response<Event>

    //GET /api/events/{event_id}/newer/ api_events_newer_read
    @GET("events/{event_id}/newer")
    suspend fun getEventsNewer(@Path("event_id") id: Long): Response<List<Event>>

    //POST /api/events/{event_id}/participants/ api_events_participants_create
    @POST("event/{event_id}/participants")
    suspend fun setEventParticipant(@Path("event_id") id: Long): Response<Event>

    //DELETE /api/events/{event_id}/participants/ api_events_participants_delete
    @DELETE("event/{event_id}/participants")
    suspend fun removeEventParticipant(@Path("event_id") id: Long): Response<Event>

    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>
}