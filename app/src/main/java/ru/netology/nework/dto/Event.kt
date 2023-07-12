package ru.netology.nework.dto

import com.google.gson.annotations.SerializedName
import ru.netology.nework.enumeration.EventType

data class Event(
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
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val speakerIds: List<Int>,
    val participantsIds: List<Int>,
    val participatedByMe: Boolean,
    val attachment: Attachment?,
    val link: String?,
    val ownedByMe: Boolean,
)
