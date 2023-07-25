package ru.netology.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Job
import ru.netology.nework.dto.Media
import ru.netology.nework.dto.MediaUpload

interface JobRepository {
    suspend fun getMyJobs(): List<Job>
    suspend fun getUserJobs(userId: Long): List<Job>
    suspend fun removeMyJobById(id: Long)
    suspend fun saveMyJob(job: Job): Job
}