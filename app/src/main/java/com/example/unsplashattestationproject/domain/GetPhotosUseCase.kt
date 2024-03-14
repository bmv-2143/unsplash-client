package com.example.unsplashattestationproject.domain

import androidx.paging.PagingData
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.room.entities.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
) {

    operator fun invoke(): Flow<PagingData<Photo>> = unsplashRepository.getPhotosFlow()
}