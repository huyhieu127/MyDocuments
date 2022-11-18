package com.huyhieu.library.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 *  @param colorRes [ID resource]
 * */
fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

/**
 *  @param colorRes [ID resource]
 * */
fun Context.colorStateList(@ColorRes colorRes: Int) =
    ContextCompat.getColorStateList(this, colorRes)

/**
 *  @param drawableRes [ID resource]
 * */
fun Context.drawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

/**
 *  @param dimenRes [ID resource]
 * */

fun Context.dimen(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun Context.dimenPx(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

/**
 *  @param attrRes [ID resource]
 * */
fun Context.typedValue(@AttrRes attrRes: Int): TypedValue {
    val outValue = TypedValue()
    theme.resolveAttribute(attrRes, outValue, true)
    return outValue
}

/**
 *  @param fontRes [ID resource]
 * */
fun Context.font(@FontRes fontRes: Int) = ResourcesCompat.getFont(this, fontRes)

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