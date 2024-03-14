package com.example.unsplashattestationproject.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedRepository @Inject constructor() {

    private val _photoDownloadCompletedEvent = MutableLiveData<Pair<Long, Uri>>()
    val photoDownloadCompletedEvent: LiveData<Pair<Long, Uri>> = _photoDownloadCompletedEvent

    fun onDownloadCompleted(id: Long, fileUri : Uri) {
        _photoDownloadCompletedEvent.value = Pair(id, fileUri)
    }

}