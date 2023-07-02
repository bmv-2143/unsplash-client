package com.example.unsplashattestationproject.presentation.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.example.unsplashattestationproject.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SnackbarFactory @Inject constructor(@ApplicationContext val context: Context) {

    fun showErrorSnackbar(
        view: View,
        message: String,
        onClick: (() -> Unit)? = null
    ) = showSnackbar(
        view,
        message,
        Color.RED,
        context.getString(R.string.activity_bottom_navigation_snackbar_view_close),
        onClick
    )

    fun showWarningSnackbar(
        view: View,
        message: String,
        onClick: (() -> Unit)? = null
    ) = showSnackbar(
        view,
        message,
        Color.YELLOW,
        context.getString(R.string.activity_bottom_navigation_snackbar_view_close),
        onClick
    )

    fun showSnackbar(
        view: View,
        message: String,
        actionButtonText: String,
        onClick: (() -> Unit)? = null
    ) = showSnackbar(view, message, null, actionButtonText, onClick)

    fun showSnackbar(
        view: View,
        message: String,
        color: Int? = null,
        actionButtonText: String?,
        onClick: (() -> Unit)? = null
    ) {
        with(
            Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_INDEFINITE
            )
        ) {
            val snackbarView = this.view
            val textView =
                snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            color?.let { textView.setTextColor(color) }
            snackbarView.setBackgroundColor(Color.parseColor(COLOR_TRANSPARENT_80))
            setAction(actionButtonText) {
                onClick?.invoke()
            }
            show()
        }
    }

    companion object {
        const val COLOR_TRANSPARENT_80 = "#CD000000"
    }
}