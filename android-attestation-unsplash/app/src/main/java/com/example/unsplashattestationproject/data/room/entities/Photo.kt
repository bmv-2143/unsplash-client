package com.example.unsplashattestationproject.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(

//    @PrimaryKey
//    @ColumnInfo(name = "uri")
//    val uri: String,

//    @ColumnInfo(name = "timestamp")
//    val timestamp: Long,

    @PrimaryKey
    @ColumnInfo(name = "uri")
    val id: String,
    val createdAt: String,
//    val updatedAt: String,
//    val width: Int,
//    val height: Int,
//    val color: String,
//    val blurHash: String?,
//    val description: String?,
//    val altDescription: String?,
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

    //val currentUserCollections: List<UnsplashCollection>,
    //val sponsorship: UnsplashSponsorship?,
    val userId: String?,
    val userName: String?,
    val userNickname: String?,

)