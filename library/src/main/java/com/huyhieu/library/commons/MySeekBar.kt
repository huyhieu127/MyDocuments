package com.huyhieu.library.commons

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import com.huyhieu.library.R
import com.huyhieu.library.extensions.drawable

class MySeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatSeekBar(context, attrs) {
    init {
        thumb = context.drawable(R.drawable.thumb_seek_bar)
        progressDrawable = context.drawable(R.drawable.progress_seek_bar)
    }
}