package com.example.unsplashattestationproject.domain

import androidx.paging.PagingData
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.room.entities.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
) {

    operator fun invoke(query: String): Flow<PagingData<Photo>> =
        unsplashRepository.searchPhotos(query)
}