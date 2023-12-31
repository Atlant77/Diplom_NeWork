package ru.netology.nework.dto

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val coords: Coordinates? = null,
    val link: String?,
    @SerializedName("likeOwnerIds")
    val likedOwnerIds: List<Int>,
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false,

)
