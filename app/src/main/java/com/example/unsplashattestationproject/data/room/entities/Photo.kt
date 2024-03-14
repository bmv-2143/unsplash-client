package com.example.unsplashattestationproject.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val remoteId: String,
    val createdAt: String,
    val updatedAt: String,
    val width: Int,
    val height: Int,
    val color: String,
    val blurHash: String?,
    val description: String?,
    val altDescription: String?,
    val urlsRaw: String,
    val urlsFull: String,
    val urlsRegular: String,
    val urlsSmall: String,
    val urlsThumb: String,
    val linksSelf: String,
    val linksHtml: String,
    val linksDownload: String,
    val linksDownloadLocation: String,
    val likes: Int,
    val likedByUser: Boolean,
    val userId: String,
    val userName: String,
    val userNickname: String,
    val userAvatar: String,
)