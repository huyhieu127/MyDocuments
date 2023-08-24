package com.huyhieu.mydocuments.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

fun ImageView.load(
    any: Any?,
    block: (RequestBuilder<Drawable>.() -> Unit)? = null
) {
    Glide.with(this).load(any).centerCrop().into(this@load)
}