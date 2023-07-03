package com.example.unsplashattestationproject.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.data.toPhoto
import com.example.unsplashattestationproject.log.TAG

abstract class BasePagingSource : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>) = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: FIRST_PAGE

        Log.e(TAG, "${::load.name}: page = $page")

        kotlin.runCatching {
            loadData(page).map { it.toPhoto() }
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

    protected abstract suspend fun loadData(page: Int): List<UnsplashPhoto>

    private companion object {
        private const val FIRST_PAGE = 0
    }
}