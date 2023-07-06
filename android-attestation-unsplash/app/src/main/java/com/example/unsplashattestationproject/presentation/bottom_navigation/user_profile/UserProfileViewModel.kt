package com.example.unsplashattestationproject.presentation.bottom_navigation.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashattestationproject.data.dto.profile.UnsplashUserProfile
import com.example.unsplashattestationproject.domain.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "USER PROFILE"
    }
    val text: LiveData<String> = _text

    private val _userProfileFlow: MutableSharedFlow<UnsplashUserProfile> =
        MutableSharedFlow()
    val userProfileFlow = _userProfileFlow.asSharedFlow()

    internal fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase()?.let { userProfile ->
                _userProfileFlow.emit(userProfile)
            }
        }
    }
}