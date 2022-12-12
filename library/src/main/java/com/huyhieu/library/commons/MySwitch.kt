package com.huyhieu.library.commons

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.huyhieu.library.R

class MySwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SwitchCompat(context, attrs) {
    init {
        thumbDrawable = ContextCompat.getDrawable(context, R.drawable.bg_switch_thumb)
        trackDrawable = ContextCompat.getDrawable(context, R.drawable.bg_switch_track)
    }
}