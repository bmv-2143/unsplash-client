package com.example.unsplashattestationproject.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashattestationproject.log.TAG

abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>) = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: FIRST_PAGE

        kotlin.runCatching {
            loadData(page)
        }.fold(
            onSuccess = { data: List<T> ->
                return LoadResult.Page(
                    data = data,
                    prevKey = if (page == FIRST_PAGE) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                )
            },
            onFailure = { exception ->
                Log.e(
                    TAG, "${::load.name}: onFailure: page = $page, error: ${exception.message}")
                return LoadResult.Error(exception)
            })
    }

    protected abstract suspend fun loadData(page: Int): List<T>

    private companion object {
        private const val FIRST_PAGE = 0
    }
}