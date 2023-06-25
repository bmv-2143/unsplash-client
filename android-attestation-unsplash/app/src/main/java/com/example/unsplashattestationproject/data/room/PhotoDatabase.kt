package com.example.unsplashattestationproject.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unsplashattestationproject.data.room.daos.PhotoDao
import com.example.unsplashattestationproject.data.room.entities.Photo

@Database(entities = [Photo::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

}