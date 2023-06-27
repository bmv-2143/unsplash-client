package com.example.unsplashattestationproject.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.room.PhotoDatabase
import com.example.unsplashattestationproject.data.room.entities.Photo
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator @Inject constructor(
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
    private val photoDatabase: PhotoDatabase

) : RemoteMediator<Int, Photo>() {

    private var nextPage: Int? = null

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Photo>
    ): MediatorResult {
        // Get the page key for the current load
        val pageKey = when (loadType) {
            LoadType.REFRESH -> {
                nextPage = null
                null
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> nextPage
        }

        // Fetch data from the Unsplash API
        val response = try {
            unsplashNetworkDataSource.getPhotos(
                page = pageKey ?: 1,
                perPage = state.config.pageSize
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }

        // Store the data in the Room database
        cacheResponseInDb(loadType, pageKey, response)

        // Return success with endOfPaginationReached set to true if no more data is available
        return MediatorResult.Success(endOfPaginationReached = response.isEmpty())
    }

    private suspend fun cacheResponseInDb(
        loadType: LoadType,
        pageKey: Int?,
        response: List<UnsplashPhoto>
    ) {
        photoDatabase.withTransaction {
            if (loadType == LoadType.REFRESH) {
                clearDatabase()
            }
            nextPage = pageKey?.plus(1)
            savePhotosToDb(response)
        }
    }

    private suspend fun clearDatabase() = photoDatabase.photoDao().clearPhotos()

    private suspend fun savePhotosToDb(unsplashPhotos: List<UnsplashPhoto>) =
        photoDatabase.photoDao().insertPhotos(unsplashPhotos.map { it.toPhoto() })

}