package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.databinding.CollectionListItemBinding
import com.example.unsplashattestationproject.log.TAG

class CollectionAdapterViewHolder(
    private val binding: CollectionListItemBinding,
    val onClick: (UnsplashCollection) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: UnsplashCollection? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(collectionItem: UnsplashCollection) {
        currentItem = collectionItem
        loadTexts(collectionItem)
        loadAvatar(collectionItem)
        loadPhoto(collectionItem.coverPhoto?.urls?.regular ?: "")
    }

    private fun loadTexts(collectionItem: UnsplashCollection) {
        binding.collectionListItemNumberOfPhotos.text =
            binding.root.context.getString(
                R.string.collection_item_number_of_photos_template,
                collectionItem.totalPhotos.toString()
            )

        binding.collectionListItemTitle.text = collectionItem.title
        binding.collectionAuthorName.text = collectionItem.user.name

        binding.collectionAuthorNickname.text =
            binding.root.context.getString(
                R.string.photo_item_author_nickname_template,
                collectionItem.user.username
            )
    }

    private fun loadAvatar(collectionItem: UnsplashCollection) {
        Glide.with(binding.root)
            .load(collectionItem.user.profileImageUrls.small)
            .circleCrop()
            .placeholder(R.drawable.photo_list_item_avatar_placeholder)
            .into(binding.collectionAuthorAvatar)
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
            .into(binding.collectionItemImage)
    }

    private fun progressBarSetVisible(binding: CollectionListItemBinding, isActive: Boolean) =
        if (isActive) {
            binding.collectionItemProgressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.collectionItemProgressBar.visibility = android.view.View.GONE
        }
}