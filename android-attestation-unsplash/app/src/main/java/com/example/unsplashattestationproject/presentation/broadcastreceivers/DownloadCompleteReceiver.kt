package com.example.unsplashattestationproject.presentation.broadcastreceivers

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.unsplashattestationproject.data.SharedRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var sharedRepository: SharedRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != DOWNLOAD_COMPLETED_ACTION)
            return

        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)

        if (id != -1L) {
            Log.d(TAG, "Download with ID $id finished!")
            sendDownloadCompleteEvent(id)
        }
    }

    private fun sendDownloadCompleteEvent(id: Long) {
        if (::sharedRepository.isInitialized) {
            sharedRepository.onDownloadCompleted(id)
        }
    }

    companion object {
        const val DOWNLOAD_COMPLETED_ACTION = "android.intent.action.DOWNLOAD_COMPLETE"
    }

}