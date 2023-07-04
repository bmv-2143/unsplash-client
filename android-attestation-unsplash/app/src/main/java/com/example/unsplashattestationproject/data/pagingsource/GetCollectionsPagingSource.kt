package com.example.unsplashattestationproject.data.pagingsource

import com.example.unsplashattestationproject.data.PAGE_SIZE
import com.example.unsplashattestationproject.data.UnsplashNetworkDataSource
import com.example.unsplashattestationproject.data.dto.photos.UnsplashCollection
import javax.inject.Inject

class GetCollectionsPagingSource @Inject constructor(
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
) : BasePagingSource<UnsplashCollection>() {

    override suspend fun loadData(page: Int): List<UnsplashCollection> =
        unsplashNetworkDataSource.getCollections(page, PAGE_SIZE)

}