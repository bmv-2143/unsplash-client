package com.example.unsplashattestationproject.presentation.broadcastreceivers

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
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
            handleDownloadResult(id, context)
        }
    }

    private fun handleDownloadResult(id: Long, context: Context?) {
        Log.d(TAG, "Download with ID $id finished!")
        val downloadFileUri = getDownloadedFileUri(context, id)

        if (downloadFileUri == null) {
            Log.e(TAG, "Downloaded file URI is null!")
            return
        }
        sendDownloadCompleteEvent(id, downloadFileUri)
    }

    private fun sendDownloadCompleteEvent(id: Long, fileUri : Uri) {
        if (::sharedRepository.isInitialized) {
            sharedRepository.onDownloadCompleted(id, fileUri)
        }
    }

    private fun getDownloadedFileUri(context: Context?, id: Long) : Uri? {
        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(id)
        val cursor = downloadManager.query(query)

        if (cursor.moveToFirst()) {
            val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (!doesColumnExist(statusIndex))
                return null

            val status = cursor.getInt(statusIndex)

            return if (status == DownloadManager.STATUS_SUCCESSFUL) {
                getUri(cursor)
            } else {
                null
            }
        }
        return null
    }

    private fun getUri(cursor : Cursor) : Uri? {
        val uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)

        if (doesColumnExist(uriIndex)) {
            val uriString = cursor.getString(uriIndex)
            val uri = Uri.parse(uriString)
            Log.d(TAG, "Downloaded file URI: $uri")
            return uri
        }
        return null
    }

    private fun doesColumnExist(columnIndex : Int) : Boolean = columnIndex != -1

    companion object {
        const val DOWNLOAD_COMPLETED_ACTION = "android.intent.action.DOWNLOAD_COMPLETE"
    }

}