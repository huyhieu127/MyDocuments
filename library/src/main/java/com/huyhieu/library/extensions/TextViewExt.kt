package com.huyhieu.library.extensions

import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.Px

fun TextView.setTextSizePx(@Px size: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat())
}