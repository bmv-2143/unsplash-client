package com.example.unsplashattestationproject.presentation.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashattestationproject.data.LocalRepository
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationActivityViewModel @Inject constructor(
    private val repository: UnsplashRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    var authCode: String = ""

    var accessToken: String = ""

    fun getAccessToken(code: String) {
        viewModelScope.launch {
            accessToken = repository.getAccessToken(code)
            Log.e(TAG, "getAccessToken: $accessToken")
        }
    }

    fun isOnboardingShowed(): Boolean = localRepository.isOnboardingShowed()

    fun saveOnboardingShowedStatus() {
        localRepository.saveOnboardingShowedStatus()
    }

}