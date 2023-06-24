package com.example.unsplashattestationproject.domain

import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: UnsplashRepository // todo: should inject pagingdatasource instead
) {

    suspend operator fun invoke(): Result<List<UnsplashPhoto>> {
        return repository.getPhotos()
    }


//    suspend operator fun invoke(
//        page: Int,
//        perPage: Int,
//        orderBy: String
//    ): Result<List<UnsplashPhoto>> {
//        return repository.getPhotos(page, perPage, orderBy)
//    }
}