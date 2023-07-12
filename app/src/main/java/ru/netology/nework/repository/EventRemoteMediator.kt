package ru.netology.nework.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nework.api.EventApi
import ru.netology.nework.dao.EventDao
import ru.netology.nework.dao.EventRemoteKeyDao
import ru.netology.nework.db.AppDb
import ru.netology.nework.entity.EventEntity
import ru.netology.nework.entity.EventRemoteKeyEntity
import ru.netology.nework.entity.toEntity
import ru.netology.nework.error.ApiError
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EventRemoteMediator @Inject constructor(
    private val service: EventApi,
    private val db: AppDb,
    private val eventDao: EventDao,
    private val eventRemoteKeyDao: EventRemoteKeyDao,
) : RemoteMediator<Int, EventEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> /* Код с лекции service.getLatest(state.config.initialLoadSize)*/
                    eventRemoteKeyDao.max()?.let { id ->
                        service.getEventsAfter(id, state.config.pageSize)
                    } ?: service.getEventsLatest(state.config.pageSize)

                LoadType.PREPEND -> {
//                    Код с лекции
                    val id = eventRemoteKeyDao.max() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    service.getEventsAfter(id, state.config.pageSize)
                    return MediatorResult.Success(true)
                }

                LoadType.APPEND -> {
                    val id = eventRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    service.getEventsBefore(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(
                response.code(),
                response.message(),
            )

            db.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        eventRemoteKeyDao.removeAll()
                        eventRemoteKeyDao.insert(
                            listOf(
                                EventRemoteKeyEntity(
                                    type = EventRemoteKeyEntity.KeyType.AFTER,
                                    id = body.first().id,
                                ),
                                EventRemoteKeyEntity(
                                    type = EventRemoteKeyEntity.KeyType.BEFORE,
                                    id = body.last().id,
                                ),
                            )
                        )
                        eventDao.removeAllEvents()
                    }

                    LoadType.PREPEND -> {
                        eventRemoteKeyDao.insert(
                            EventRemoteKeyEntity(
                                type = EventRemoteKeyEntity.KeyType.AFTER,
                                id = body.first().id,
                            )
                        )
                    }

                    LoadType.APPEND -> {
                        eventRemoteKeyDao.insert(
                            EventRemoteKeyEntity(
                                type = EventRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id,
                            )
                        )
                    }
                }
                eventDao.insert(body.toEntity())
            }
            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}