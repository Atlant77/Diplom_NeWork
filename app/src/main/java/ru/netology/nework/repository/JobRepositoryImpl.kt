package ru.netology.nework.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.netology.nework.api.JobApi
import ru.netology.nework.dto.Job
import ru.netology.nework.entity.toEntity
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import java.io.IOException
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val apiService: JobApi,
    @ApplicationContext
    private val context: Context,
) : JobRepository {
    override suspend fun getMyJobs(): List<Job> {
        try {
            val response = apiService.getMyJobs()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            return response.body()!!
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getUserJobs(userId: Long): List<Job> {
        try {
            val response = apiService.getUserJobsById(userId)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body()!!
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeMyJobById(id: Long) {
        try {
            val response = apiService.removeMyJobById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveMyJob(job: Job): Job {
        try {
            val response = apiService.saveMyJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
            return response.body()!!
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}