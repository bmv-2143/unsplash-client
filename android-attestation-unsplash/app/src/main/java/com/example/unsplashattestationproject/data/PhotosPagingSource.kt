package com.example.unsplashattestationproject.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.log.TAG
import javax.inject.Inject

class PhotosPagingSource @Inject constructor(private val unsplashRepository: UnsplashRepository) :
    PagingSource<Int, UnsplashPhoto>() {

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>) = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val page = params.key ?: FIRST_PAGE

        Log.e(TAG, "${::load.name}: page = $page")

        kotlin.runCatching {
            val result = unsplashRepository.getPhotos(page)
            result
        }.fold(
            onSuccess = { photos: List<UnsplashPhoto> ->
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