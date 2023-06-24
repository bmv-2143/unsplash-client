package com.example.unsplashattestationproject.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.unsplashattestationproject.data.PhotosPagingSource
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val photosPagingSource: PhotosPagingSource
) {

    operator fun invoke(): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { photosPagingSource }
        ).flow

    }
}