package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.domain.GetPhotosUseCase
import com.example.unsplashattestationproject.domain.SearchPhotosUseCase
import com.example.unsplashattestationproject.log.TAG
import com.example.unsplashattestationproject.presentation.bottom_navigation.model.toPhotoListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    getPhotosUseCase: GetPhotosUseCase,
    val searchPhotosUseCase: SearchPhotosUseCase
) :
    ViewModel() {

    var isSearchOpened = false
        private set
    var currentQuery : String? = null
        private set

    private val pagedPhotosFlow = cacheInPhotoPagingFlow(getPhotosUseCase())

    private fun cacheInPhotoPagingFlow(input : Flow<PagingData<Photo>>) : Flow<PagingData<PhotoListItemUiModel>> {
        return input.map { pagingData: PagingData<Photo> ->
            pagingData.map { photo: Photo ->
                photo.toPhotoListItemUiModel()
            }
        }.cachedIn(viewModelScope)
    }

    fun getPhotosPagedFlow(): Flow<PagingData<PhotoListItemUiModel>> = pagedPhotosFlow

    private val _uiStateFlow = MutableSharedFlow<PhotoListFragmentState>(
        replay = 100,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiStatesFlow = _uiStateFlow.asSharedFlow()

    private var dataIsLoading: Boolean = false

    fun handleAdapterUpdates(loadStates: CombinedLoadStates) {
        updateDataIsLoading(loadStates)
        viewModelScope.launch {
            when (loadStates.refresh) {
                is LoadState.Loading -> {
                    _uiStateFlow.emit(PhotoListFragmentState.FirstPageLoading)
                }

                is LoadState.NotLoading -> {
                    _uiStateFlow.emit(PhotoListFragmentState.FirstPageNotLoading)
                    handleNextPageLoading(loadStates)
                }

                is LoadState.Error -> Log.e(TAG, "handleAdapterUpdates: ${loadStates.refresh}")
            }
        }
    }

    private suspend fun handleNextPageLoading(loadStates: CombinedLoadStates) {
        when (loadStates.append) {
            is LoadState.Loading -> {
                _uiStateFlow.emit(PhotoListFragmentState.NextPageLoading)
            }

            is LoadState.NotLoading -> {
                _uiStateFlow.emit(PhotoListFragmentState.NextPageNotLoading)
            }

            is LoadState.Error -> Log.e(TAG, "handleAdapterUpdates: ${loadStates.refresh}")
        }
    }

    private fun updateDataIsLoading(loadStates: CombinedLoadStates) {
        dataIsLoading =
            (loadStates.refresh is LoadState.Loading) || (loadStates.append is LoadState.Loading)
    }

    private val _searchResults = MutableStateFlow<PagingData<PhotoListItemUiModel>>(PagingData.empty())
    val searchResults = _searchResults.asStateFlow()

    private fun startSearch(query: String) {
        currentQuery = query
        viewModelScope.launch {
            cacheInPhotoPagingFlow(searchPhotosUseCase(query)).collectLatest {
                _searchResults.value = it
            }
        }
    }

    fun onSearchOpened() {
        viewModelScope.launch {
            isSearchOpened = true
            currentQuery = ""
        }
    }

    fun onQueryTextChanged(newText: String) {
        currentQuery = newText
    }

    fun onSearchSubmitted(query: String) {
        startSearch(query)
        viewModelScope.launch {
            isSearchOpened = true
        }
    }

    fun onSearchClosed() {
        viewModelScope.launch {
            isSearchOpened = false
            currentQuery = null
        }
    }

}