package com.huyhieu.mydocuments.utils.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.drawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

fun Context.dimen(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun Context.typedValue(@AttrRes attrRes: Int): TypedValue {
    val outValue = TypedValue()
    theme.resolveAttribute(attrRes, outValue, true)
    return outValue
}