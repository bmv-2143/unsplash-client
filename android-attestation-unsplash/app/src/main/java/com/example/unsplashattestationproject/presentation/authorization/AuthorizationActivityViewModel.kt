package com.example.unsplashattestationproject.presentation.authorization

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashattestationproject.data.LocalRepository
import com.example.unsplashattestationproject.data.network.AuthQuery
import com.example.unsplashattestationproject.data.network.AuthQuery.Companion.PARAM_CLIENT_ID
import com.example.unsplashattestationproject.data.network.AuthQuery.Companion.PARAM_REDIRECT_URI
import com.example.unsplashattestationproject.data.network.AuthQuery.Companion.PARAM_RESPONSE_TYPE
import com.example.unsplashattestationproject.data.network.AuthQuery.Companion.PARAM_SCOPE
import com.example.unsplashattestationproject.data.network.AuthQuery.Companion.VAL_PUBLIC
import com.example.unsplashattestationproject.data.network.AuthQuery.Companion.VAL_RESPONSE_TYPE_CODE
import com.example.unsplashattestationproject.domain.AuthorizeUserUseCase
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationActivityViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val authorizeUserUseCase: AuthorizeUserUseCase,
) : ViewModel() {

    private var _authorizationState = MutableSharedFlow<Boolean>()
    val authorizationState = _authorizationState.asSharedFlow()

    var authCode: String = ""
        set(value) {
            field = value
            Log.d(TAG, "authCode: $value")
        }

    fun authorizeUser() {
        viewModelScope.launch {
            _authorizationState.emit(authorizeUserUseCase(authCode))
        }
    }

    fun isOnboardingShowed(): Boolean = localRepository.isOnboardingShowed()

    fun saveOnboardingShowedStatus() {
        localRepository.saveOnboardingShowedStatus()
    }

    fun composeBrowserAuthUrl(): Uri =
        Uri.parse(AuthQuery.AUTH_URL)
            .buildUpon()
            .appendQueryParameter(
                PARAM_CLIENT_ID,
                AuthQuery.VAL_ACCESS_KEY
            )
            .appendQueryParameter(
                PARAM_REDIRECT_URI,
                AuthQuery.VAL_REDIRECT_URL
            )
            .appendQueryParameter(PARAM_RESPONSE_TYPE, VAL_RESPONSE_TYPE_CODE)
            .appendQueryParameter(PARAM_SCOPE, VAL_PUBLIC)
            .build()
}