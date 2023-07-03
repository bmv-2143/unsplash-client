package com.example.unsplashattestationproject.data.pagingsource

import com.example.unsplashattestationproject.data.PAGE_SIZE
import com.example.unsplashattestationproject.data.UnsplashNetworkDataSource
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import javax.inject.Inject

class GetPhotosPagingSource @Inject constructor(
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
) : BasePagingSource() {

    override suspend fun loadData(page: Int): List<UnsplashPhoto> =
        unsplashNetworkDataSource.getPhotos(page, PAGE_SIZE)

}