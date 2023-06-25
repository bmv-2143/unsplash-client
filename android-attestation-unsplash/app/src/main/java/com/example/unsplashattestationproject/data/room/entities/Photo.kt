package com.example.unsplashattestationproject.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
data class Photo(

    @PrimaryKey
    @ColumnInfo(name = "uri")
    val uri: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long

)