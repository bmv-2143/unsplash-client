package com.example.unsplashattestationproject.presentation.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import com.example.unsplashattestationproject.App
import com.example.unsplashattestationproject.BuildConfig
import com.example.unsplashattestationproject.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationMaker @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {

    @SuppressLint("MissingPermission")
    fun makeNotification(title: String, contentText: String, fileUri: Uri) {
        val notification = NotificationCompat.Builder(applicationContext, App.UNSPLASH_APP_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground_96)
            .setContentTitle(title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(makePendingIntent(fileUri))
            .setAutoCancel(true).build()

        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
    }

    private fun makePendingIntent(fileUri: Uri): PendingIntent {
        val uri = FileProvider.getUriForFile(
            applicationContext,
            BuildConfig.DOWNLOADED_FILE_PROVIDER_AUTHORITY,
            File(fileUri.path!!)
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "image/*")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        val intentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(applicationContext, 0, intent, intentFlag)
    }

    companion object {
        const val NOTIFICATION_ID = 1000
    }
}