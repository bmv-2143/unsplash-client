package com.example.unsplashattestationproject.data.room.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.unsplashattestationproject.data.room.entities.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Photo>>

    @Insert(entity = Photo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<Photo>)

    @Query("SELECT * FROM photos ORDER BY id ASC")
    fun getPhotos(): PagingSource<Int, Photo>

    @Query("SELECT * FROM photos WHERE remoteId IN (:ids)")
    fun getPhotosByIds(ids: List<String>): List<Photo>

    @Query("DELETE FROM photos")
    suspend fun clearPhotos()

}