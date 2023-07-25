package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nework.dto.Job

interface JobApi {
    //    Jobs

    //GET /api/my/jobs/ api_my_jobs_read
    @GET("my/jobs/")
    suspend fun getMyJobs(): Response<List<Job>>

    //POST /api/my/jobs/ api_my_jobs_create
    @POST("my/jobs/")
    suspend fun saveMyJob(@Body job: Job): Response<Job>

    //DELETE /api/my/jobs/{job_id}/ api_my_jobs_delete
    @DELETE("my/jobs/{job_id}")
    suspend fun removeMyJobById(@Path("job_id") id: Long): Response<Unit>

    //GET /api/{user_id}/jobs/ api_jobs_read
    @GET("{user_id}/jobs/")
    suspend fun getUserJobsById(@Path("user_id") id: Long): Response<List<Job>>
}