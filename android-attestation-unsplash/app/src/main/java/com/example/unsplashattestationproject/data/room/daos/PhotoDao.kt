package com.example.unsplashattestationproject.data.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.unsplashattestationproject.data.room.entities.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo_table ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Photo>>

    @Insert(entity = Photo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: Photo)

}