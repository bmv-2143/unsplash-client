package com.example.unsplashattestationproject.presentation.bottom_navigation

import com.example.unsplashattestationproject.presentation.permissions.PermissionRequester

fun interface PermissionRequestProvider {

    fun getPermissionRequester(): PermissionRequester

}
