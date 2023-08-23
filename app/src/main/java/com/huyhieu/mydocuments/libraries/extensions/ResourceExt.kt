package com.huyhieu.mydocuments.libraries.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 *  @param colorRes [ID resource]
 * */
fun Context?.color(@ColorRes colorRes: Int): Int {
    this ?: return 0
    return ContextCompat.getColor(this, colorRes)
}

/**
 *  @param colorRes [ID resource]
 * */
fun Context?.colorStateList(@ColorRes colorRes: Int): ColorStateList? {
    this ?: return null
    return ContextCompat.getColorStateList(this, colorRes)
}


/**
 *  @param drawableRes [ID resource]
 * */
fun Context?.drawable(@DrawableRes drawableRes: Int): Drawable? {
    this ?: return null
    return ContextCompat.getDrawable(this, drawableRes)
}

/**
 *  @param dimenRes [ID resource]
 * */

fun Context?.dimen(@DimenRes dimenRes: Int): Float {
    this ?: return 0F
    return resources.getDimension(dimenRes)
}

fun Context?.dimenPx(@DimenRes dimenRes: Int): Int {
    this ?: return 0
    return resources.getDimensionPixelSize(dimenRes)
}

/**
 *  @param attrRes [ID resource]
 * */
fun Context?.typedValue(@AttrRes attrRes: Int): TypedValue {
    this ?: return TypedValue()
    val outValue = TypedValue()
    theme.resolveAttribute(attrRes, outValue, true)
    return outValue
}

/**
 *  @param fontRes [ID resource]
 * */
fun Context?.font(@FontRes fontRes: Int): Typeface? {
    this ?: return null
    return ResourcesCompat.getFont(this, fontRes)
}

/**
 * @param colorRes [ID resource]*/
fun View.backgroundTint(@ColorRes colorRes: Int) {
    backgroundTintList = context.colorStateList(colorRes)
}

fun View.backgroundTint(colorStateList: ColorStateList?) {
    backgroundTintList = colorStateList
}

/**
 * @param color context.color([ID resource])*/
fun ImageView.tint(@ColorInt color: Int) {
    setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY)
}

/**
 * @param color context.color([ID resource])*/
fun ImageView.tintVector(@ColorInt color: Int) {
    setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}