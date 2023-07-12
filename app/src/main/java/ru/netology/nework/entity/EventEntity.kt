package ru.netology.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.dto.Coordinates
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.ListIds
import ru.netology.nework.enumeration.EventType

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates?,
    val type: EventType,
    val likeOwnerIds: ListIds,
    val likedByMe: Boolean,
    val speakerIds: ListIds,
    val participantsIds: ListIds,
    val participatedByMe: Boolean,
    @Embedded(prefix = "event_")
    val attachment: AttachmentEmbeddable?,
    val link: String?,
    val ownedByMe: Boolean,
) {
    fun toDto() = Event(
        id, authorId, author, authorAvatar, authorJob, content, datetime, published,
        coords, type, likeOwnerIds.list, likedByMe, speakerIds.list,
        participantsIds.list, participatedByMe, attachment?.toDto(), link, ownedByMe
    )

    companion object {
        fun fromDto(dto: Event) =
            EventEntity(
                dto.id, dto.authorId, dto.author, dto.authorAvatar, dto.authorJob,
                dto.content, dto.datetime, dto.published, dto.coords, dto.type,
                ListIds(dto.likeOwnerIds), dto.likedByMe, ListIds(dto.speakerIds),
                ListIds(dto.participantsIds), dto.participatedByMe,
                AttachmentEmbeddable.fromDto(dto.attachment), dto.link, dto.ownedByMe
            )

    }
}

fun List<EventEntity>.toDto(): List<Event> = map(EventEntity::toDto)
fun List<Event>.toEntity(): List<EventEntity> = map(EventEntity.Companion::fromDto)
