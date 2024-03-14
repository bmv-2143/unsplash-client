package com.example.unsplashattestationproject.data.pagingsource

import com.example.unsplashattestationproject.data.PAGE_SIZE
import com.example.unsplashattestationproject.data.UnsplashNetworkDataSource
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.data.toPhoto
import javax.inject.Inject

class GetLikedPhotosPagingSource @Inject constructor(
    private val username: String,
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
) : BasePagingSource<Photo>() {

    override suspend fun loadData(page: Int): List<Photo> =
        unsplashNetworkDataSource.getLikedPhotos(username, page, PAGE_SIZE).map { it.toPhoto() }

}