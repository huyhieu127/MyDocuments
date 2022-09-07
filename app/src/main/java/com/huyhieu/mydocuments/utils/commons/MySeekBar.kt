package com.huyhieu.mydocuments.utils.commons

import android.content.Context
import android.util.AttributeSet
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.utils.extensions.drawable

class MySeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : androidx.appcompat.widget.AppCompatSeekBar(context, attrs) {
    init {
        thumb = context.drawable(R.drawable.thumb_seek_bar)
        progressDrawable = context.drawable(R.drawable.progress_seek_bar)
    }
}