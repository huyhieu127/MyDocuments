package com.huyhieu.mydocuments.models

import android.net.Uri

class YouTubeForm(
    val uri: Uri = Uri.EMPTY,
    val urlThumbnail: String = "",
    val title: String = "",
    val urlAuthorAvatar: String = "",
    val authorName: String = "",
    val duration: Int = 0,
    val size: Int = 0,
)