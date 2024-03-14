package com.example.unsplashattestationproject.presentation.permissions

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionRequester(private val activity: ComponentActivity) {

    private val _permissionState = MutableStateFlow(PermissionState.Initial)
    val permissionState = _permissionState.asStateFlow()

    enum class PermissionState {
        Initial,
        Requesting,
        AllGranted,
        NotAllGranted
    }

    fun checkAndRequestPermissions(requiredPermissions: Array<String>) {
        if (requiredPermissions.isEmpty()) {
            _permissionState.value = PermissionState.AllGranted
            return
        }

        if (areAllPermissionsGranted(requiredPermissions)) {
            _permissionState.value = PermissionState.AllGranted
        } else {
            _permissionState.value = PermissionState.Requesting
            launcher.launch(requiredPermissions)
        }
    }

    private val launcher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissionsMap ->
            if (permissionsMap.values.isNotEmpty() &&
                permissionsMap.values.all { isGranted: Boolean -> isGranted }
            ) {
                _permissionState.value = PermissionState.AllGranted
            } else {
                Log.e(TAG, "Not all permissions granted")
                _permissionState.value = PermissionState.NotAllGranted
            }
        }

    fun areAllPermissionsGranted(requiredPermissions: Array<String>) =
        requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

}