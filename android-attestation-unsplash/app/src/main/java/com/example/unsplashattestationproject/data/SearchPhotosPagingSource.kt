package com.example.unsplashattestationproject.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.log.TAG
import javax.inject.Inject

// todo: dry, @see PhotosPagingSource
class SearchPhotosPagingSource @Inject constructor(
    private val query: String,
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>) = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: FIRST_PAGE

        Log.e(TAG, "${::load.name}: page = $page")

        kotlin.runCatching {
            val result = unsplashNetworkDataSource.search(query, page, PAGE_SIZE)
//            val result = photoDataProvider.provide(page, PAGE_SIZE)
            result.map { it.toPhoto() }
        }.fold(
            onSuccess = { photos: List<Photo> ->
                Log.e(TAG, "${::load.name}: page = $page - SUCCESS")
                return LoadResult.Page(
                    data = photos,
                    prevKey = if (page == FIRST_PAGE) null else page - 1,
                    nextKey = if (photos.isEmpty()) null else page + 1
                )
            },
            onFailure = { exception ->
                Log.e(TAG, "${::load.name}: page = $page - FAIL")
                Log.e(TAG, "${::load.name}: onFailure: ${exception.message}")
                return LoadResult.Error(exception)
            })
    }

    private companion object {
        private const val FIRST_PAGE = 0
    }
}