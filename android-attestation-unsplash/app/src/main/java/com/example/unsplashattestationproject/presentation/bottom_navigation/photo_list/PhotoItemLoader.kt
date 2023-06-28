package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding
import com.example.unsplashattestationproject.log.TAG


class PhotoItemLoader(val binding: PhotoListItemBinding) {

    internal fun loadData(photoItem: PhotoListItemUiModel) {
        val photoItemLoader = PhotoItemLoader(binding)
        photoItemLoader.setPhotoTexts(photoItem)
        photoItemLoader.updateLikeButtonState(photoItem)
        photoItemLoader.loadAuthorAvatar(photoItem.authorAvatar)
        photoItemLoader.loadPhoto(photoItem.imageUrl)
    }

    private fun setPhotoTexts(photoItem: PhotoListItemUiModel) {
        binding.photoListItemAuthorName.text = photoItem.authorUsername
        binding.photoListItemAuthorNickname.text = photoItem.authorName
        binding.photoListItemLikeCount.text = photoItem.likes.toString()
    }

    private fun updateLikeButtonState(photoItem: PhotoListItemUiModel) {
        binding.photoListItemLikeButton.setImageResource(
            if (photoItem.likedByUser) {
                R.drawable.photo_list_item_favorite_filled
            } else {
                R.drawable.photo_list_item_favorite_empty
            }
        )
    }

    private fun loadAuthorAvatar(avatarUrl: String) {
        Glide.with(binding.root.context)
            .load(avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.photo_list_item_avatar_placeholder)
            .into(binding.photoListItemAuthorAvatar)
    }

    private fun loadPhoto(imageUrl: String) {
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