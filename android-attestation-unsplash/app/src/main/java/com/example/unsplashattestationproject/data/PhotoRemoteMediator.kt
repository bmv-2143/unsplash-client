package com.example.unsplashattestationproject.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.unsplashattestationproject.data.room.entities.Photo
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator @Inject constructor(
    private val unsplashRepository: UnsplashRepository
) : RemoteMediator<Int, Photo>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Photo>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    state.pages.indexOfLast { it.data.contains(lastItem) } + 2
                }
            }

            val photos = unsplashRepository.getPhotos(page)
            unsplashRepository.savePhotosToDb(photos)
            MediatorResult.Success(endOfPaginationReached = photos.isEmpty())

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}