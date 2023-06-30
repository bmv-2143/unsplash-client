package com.example.unsplashattestationproject.data.downloads

import android.app.DownloadManager
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import java.io.File
import javax.inject.Inject

class UnsplashDownloader @Inject constructor(
    private val downloadManager: DownloadManager
) : Downloader {

    override fun downloadFile(url: String, accessToken : String): Long {
        val fileName = getFileNameFromUri(url)
        val request = DownloadManager.Request(url.toUri())
            .setMimeType(parseMimeType(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .addRequestHeader("Authorization", "Bearer $accessToken")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        return downloadManager.enqueue(request)
    }

    private fun parseMimeType(url: String): String {
        val file = File(url)
        val map = MimeTypeMap.getSingleton()
        val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
        var type = map.getMimeTypeFromExtension(ext)
        type = type ?: "*/*"
        return type
    }

    private fun getFileNameFromUri(url: String): String {
        return url.substring(url.lastIndexOf('/') + 1, url.length)
    }
}