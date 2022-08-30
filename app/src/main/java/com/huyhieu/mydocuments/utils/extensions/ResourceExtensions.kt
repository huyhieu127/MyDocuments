package com.huyhieu.mydocuments.utils.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.*
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.colorStateList(@ColorRes colorRes: Int) =
    ContextCompat.getColorStateList(this, colorRes)

fun Context.drawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

fun Context.dimen(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun Context.typedValue(@AttrRes attrRes: Int): TypedValue {
    val outValue = TypedValue()
    theme.resolveAttribute(attrRes, outValue, true)
    return outValue
}

fun View.backgroundTint(@ColorRes colorRes: Int) {
    backgroundTintList = context.colorStateList(colorRes)
}

fun View.backgroundTint(colorStateList: ColorStateList?) {
    backgroundTintList = colorStateList
}


fun ImageView.tint(@ColorInt color: Int) {
    setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY)
}

fun ImageView.tintVector(@ColorInt color: Int) {
    setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}