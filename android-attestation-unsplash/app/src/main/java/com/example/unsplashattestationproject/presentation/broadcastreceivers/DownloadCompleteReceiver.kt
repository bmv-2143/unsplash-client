package com.example.unsplashattestationproject.presentation.broadcastreceivers

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DOWNLOAD_COMPLETED_ACTION) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)

            if (id != -1L) {
                Log.d(TAG, "Download with ID $id finished!")
            }
        }
    }

    companion object {
        const val DOWNLOAD_COMPLETED_ACTION = "android.intent.action.DOWNLOAD_COMPLETE"
    }

}