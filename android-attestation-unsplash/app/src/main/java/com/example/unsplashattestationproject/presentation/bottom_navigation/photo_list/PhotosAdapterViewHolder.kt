package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding
import com.example.unsplashattestationproject.log.TAG

class PhotosAdapterViewHolder(
    private val binding: PhotoListItemBinding,
    val onClick: (UnsplashPhoto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: UnsplashPhoto? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(photoItem: UnsplashPhoto) {
        currentItem = photoItem
        setPhotoTexts(photoItem)
        loadCharacterImage(photoItem.urls.full)
    }

    private fun setPhotoTexts(photoItem: UnsplashPhoto) {
        binding.photoListItemAuthorName.text = photoItem.user.name
        binding.photoListItemAuthorNickname.text = photoItem.user.username
        binding.photoListItemLikeCount.text = photoItem.likes.toString()
    }

    private fun loadCharacterImage(imageUrl: String) {

        progressBarSetVisible(binding, true)

        Glide.with(binding.root.context)
            .load(imageUrl)
            .placeholder(R.drawable.photo_list_item_image_placeholder)
            .listener(object : RequestListener<Drawable> {

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBarSetVisible(binding, false)
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "Glide onLoadFailed: $imageUrl")
                    return false
                }
            })
            .into(binding.photoListItemImage)
    }

    private fun progressBarSetVisible(binding: PhotoListItemBinding, isActive: Boolean) =
        if (isActive) {
            binding.photoListItemProgressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.photoListItemProgressBar.visibility = android.view.View.GONE
        }

}