package ru.netology.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.Media
import ru.netology.nework.dto.MediaUpload

interface EventRepository {
    val data: Flow<PagingData<Event>>
    suspend fun getAllEvents()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(event: Event, upload: MediaUpload?)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun upload(upload: MediaUpload): Media
}