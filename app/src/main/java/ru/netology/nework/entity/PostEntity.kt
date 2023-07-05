package ru.netology.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.dto.Coordinates
import ru.netology.nework.dto.ListIds
import ru.netology.nework.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val coords: Coordinates?,
    val link: String?,
    val likedOwnerIds: ListIds,
    val mentionIds: ListIds,
    val mentionedMe: Boolean,
    val likesByMe: Boolean,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    val ownerByMe: Boolean,
) {
    fun toDto() = Post(
        id, authorId, author, authorAvatar, authorJob, content, published,
        coords, link, likedOwnerIds.list, mentionIds.list, mentionedMe, likesByMe,
        attachment?.toDto(), ownerByMe
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar, dto.authorJob,
                dto.content, dto.published, dto.coords, dto.link, ListIds(dto.likedOwnerIds),
                ListIds(dto.mentionIds), dto.mentionedMe, dto.likedByMe,
                AttachmentEmbeddable.fromDto(dto.attachment), dto.ownedByMe)

    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
