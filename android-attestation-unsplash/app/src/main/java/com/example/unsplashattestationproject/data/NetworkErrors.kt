package com.example.unsplashattestationproject.data

sealed class NetworkErrors(val message: String) {
    class ForbiddenApiRateExceeded(message: String) : NetworkErrors(message)
    class Unauthorized(message: String) : NetworkErrors(message)
    class HttpError(message: String) : NetworkErrors(message)
    class NoInternetConnection(message: String) : NetworkErrors(message)
}