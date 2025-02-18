package com.example.unsplashattestationproject.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
) {

    operator fun invoke(): Flow<PagingData<UnsplashCollection>> =
        unsplashRepository.getCollections()
            .map { pagingData ->
                pagingData.map { collection ->
                    collection
                }
            }
}