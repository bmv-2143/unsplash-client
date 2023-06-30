package com.example.unsplashattestationproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedRepository @Inject constructor() {

    private val _photoDownloadCompletedEvent = MutableLiveData<Long>()
    val photoDownloadCompletedEvent: LiveData<Long> = _photoDownloadCompletedEvent

    fun onDownloadCompleted(id: Long) {
        _photoDownloadCompletedEvent.value = id
    }

}