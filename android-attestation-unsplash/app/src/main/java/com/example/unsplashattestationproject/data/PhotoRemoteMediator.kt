package com.example.unsplashattestationproject.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.room.PhotoDatabase
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.data.room.entities.RemoteKeys
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val UNSPLASH_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator @Inject constructor(
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
    private val photoDatabase: PhotoDatabase

) : RemoteMediator<Int, Photo>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Photo>): MediatorResult {

        Log.e(TAG, "load: loadType = $loadType, state = $state")

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val closestRemoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                closestRemoteKeys?.nextKey?.minus(1) ?: UNSPLASH_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val firstItemRemoteKeys = getRemoteKeyForFirstItem(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = firstItemRemoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = firstItemRemoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val lastItemRemoteKeys = getRemoteKeyForLastItem(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = lastItemRemoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = lastItemRemoteKeys != null)
                }
                nextKey
            }
        }

//        val apiQuery = query + IN_QUALIFIER

        try {
            val photos = unsplashNetworkDataSource.getPhotos(page, state.config.pageSize)

//            val repos = apiResponse.items

            val endOfPaginationReached = photos.isEmpty()

            photoDatabase.withTransaction {

                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    photoDatabase.remoteKeysDao().clearRemoteKeys()
                    photoDatabase.photoDao().clearPhotos()
                }
                val prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = photos.map {
                    RemoteKeys(photoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                photoDatabase.remoteKeysDao().insertAll(keys)
                photoDatabase.photoDao().insertPhotos(photos.map { it.toPhoto() })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Photo>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { photo ->
                // Get the remote keys of the last item retrieved
                photoDatabase.remoteKeysDao().remoteKeysPhotoId(photo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Photo>): RemoteKeys? {

        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { photo ->

                // Get the remote keys of the first items retrieved
                photoDatabase.remoteKeysDao().remoteKeysPhotoId(photo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Photo>
    ): RemoteKeys? {

        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { photoId ->
                photoDatabase.remoteKeysDao().remoteKeysPhotoId(photoId)
            }
        }
    }

    private suspend fun clearDatabase() = photoDatabase.photoDao().clearPhotos()

    private suspend fun savePhotosToDb(unsplashPhotos: List<UnsplashPhoto>) =
        photoDatabase.photoDao().insertPhotos(unsplashPhotos.map { it.toPhoto() })

}