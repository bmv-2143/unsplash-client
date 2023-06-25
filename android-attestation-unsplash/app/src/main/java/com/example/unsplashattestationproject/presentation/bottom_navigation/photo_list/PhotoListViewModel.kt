package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.domain.GetPhotosUseCase
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(getPhotosUseCase: GetPhotosUseCase) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "PHOTO LIST"
    }
    val dummyText: LiveData<String> = _text

    // region Paged Photos FLow
    private val pagedPhotosFlow =
        getPhotosUseCase().cachedIn(viewModelScope)

    fun getPhotosPagedFlow(): Flow<PagingData<UnsplashPhoto>> {
        Log.e(TAG, "getPhotosPagedFlow...")
        return pagedPhotosFlow
    }
    // endregion


    private val _uiStateFlow = MutableSharedFlow<PhotoListFragmentState>(
        replay = 100,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiStatesFlow = _uiStateFlow.asSharedFlow()

    var dataIsLoading: Boolean = false
        private set

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

                is LoadState.Error -> {
                    _uiStateFlow.emit(PhotoListFragmentState.FirstPageLoadError)
                }
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

            is LoadState.Error -> {
                _uiStateFlow.emit(PhotoListFragmentState.NextPageLoadError)
            }
        }
    }

    private fun updateDataIsLoading(loadStates: CombinedLoadStates) {
        dataIsLoading =
            (loadStates.refresh is LoadState.Loading) || (loadStates.append is LoadState.Loading)
    }
}