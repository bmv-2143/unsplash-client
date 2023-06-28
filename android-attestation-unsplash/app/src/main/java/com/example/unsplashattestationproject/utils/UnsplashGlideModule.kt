package com.example.unsplashattestationproject.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.module.AppGlideModule
import java.io.File

@GlideModule
class UnsplashGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val cacheDirectory = File(context.cacheDir, GLIDE_CACHE_DIR)
        val diskCacheFactory = DiskLruCacheFactory(cacheDirectory.absolutePath, GLIDE_DISK_CACHE_SIZE)
        builder.setDiskCache(diskCacheFactory)
    }

    companion object {
        const val GLIDE_CACHE_DIR = "glide_cache"
        const val GLIDE_DISK_CACHE_SIZE: Long = 1024 * 1024 * 500 // 500 MB
    }
}