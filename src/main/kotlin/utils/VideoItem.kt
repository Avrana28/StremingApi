package com.streaming.utils

import kotlinx.serialization.Serializable

@Serializable
data class VideoItem(
    val id: String,
    val title: String,
    val streamUrl: String,
    val thumbnailUrl: String?
)