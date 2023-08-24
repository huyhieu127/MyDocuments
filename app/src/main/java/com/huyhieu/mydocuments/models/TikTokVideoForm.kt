package com.huyhieu.mydocuments.models

import android.graphics.Bitmap

data class TikTokVideoForm(
    val url: String = "",
    var thumbnail: Bitmap? = null,
    var timePlayed: Long = 0,
    var isPlaying: Boolean = false,
)