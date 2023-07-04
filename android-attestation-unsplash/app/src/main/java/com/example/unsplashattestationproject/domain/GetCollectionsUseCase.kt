package com.example.unsplashattestationproject.domain

import androidx.paging.PagingData
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.photos.UnsplashCollection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
) {

    operator fun invoke(): Flow<PagingData<UnsplashCollection>> =
        unsplashRepository.getCollections()
}