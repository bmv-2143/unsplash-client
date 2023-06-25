package com.example.unsplashattestationproject.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.unsplashattestationproject.data.PhotoRemoteMediator
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.room.entities.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
    private val photoRemoteMediator: PhotoRemoteMediator
) {

    @OptIn(ExperimentalPagingApi::class)
    operator fun invoke(): Flow<PagingData<Photo>> { // TODO: return check type
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = photoRemoteMediator,
            pagingSourceFactory = { unsplashRepository.getPhotosFromDb() }
        ).flow
    }
}