package com.example.unsplashattestationproject.presentation.utils

import com.example.unsplashattestationproject.data.dto.photos.Tag

fun getFormattedTags(tags: List<Tag>): String = tags.map { it.title }.joinToString { "#$it" }