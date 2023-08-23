package com.huyhieu.mydocuments.libraries.extensions

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.dimenPx
import com.huyhieu.mydocuments.libraries.extensions.font
import com.huyhieu.mydocuments.libraries.utils.CustomTypefaceSpan

/**
 * Multiple style text*/
fun TextView.setSpannable(
    content: String,
    subText: String,
    @DimenRes textSize: Int = 0,
    @FontRes fontId: Int = 0,//R.font.font_svn_gilroy_semibold
    @ColorRes colorId: Int = R.color.colorPrimary,
    actionClick: ((subText: String) -> Unit)? = null
): TextView {
    /*Set style*/
    val spannable = SpannableString(content).toSpannable(
        context = context,
        subText = subText,
        textSize = textSize,
        fontId = fontId,
        colorId = colorId,
        actionClick = actionClick
    )
    return applySpannable(spannable)
}

fun TextView.addSpannable(
    subText: String,
    @DimenRes textSize: Int = 0,
    @FontRes fontId: Int = 0,//R.font.font_svn_gilroy_semibold
    @ColorRes colorId: Int = R.color.colorPrimary,
    actionClick: ((subText: String) -> Unit)? = null
): TextView {
    /*Set style*/
    val spannable = (this.text as SpannableString).toSpannable(
        context = context,
        subText = subText,
        textSize = textSize,
        fontId = fontId,
        colorId = colorId,
        actionClick = actionClick
    )
    return applySpannable(spannable)
}

fun String.toSpannable(
    context: Context,
    subText: String,
    @DimenRes textSize: Int = 0,
    @FontRes fontId: Int = 0,//R.font.font_svn_gilroy_semibold
    @ColorRes colorId: Int = R.color.colorPrimary,
    actionClick: ((subText: String) -> Unit)? = null
): SpannableString {
    /*Set style*/
    return SpannableString(this).toSpannable(
        context = context,
        subText = subText,
        textSize = textSize,
        fontId = fontId,
        colorId = colorId,
        actionClick = actionClick
    )
}

fun SpannableString.toSpannable(
    context: Context,
    subText: String,
    @DimenRes textSize: Int = 0,
    @FontRes fontId: Int = 0,//R.font.font_svn_gilroy_semibold
    @ColorRes colorId: Int = R.color.colorPrimary,
    actionClick: ((subText: String) -> Unit)? = null
): SpannableString {
    if (this.isEmpty() || subText.isEmpty()) return this
    val startIdx = this.indexOf(subText)
    val endIdx = startIdx + subText.length
    /*Set style*/
    return this.apply {
        //Set text size
        if (textSize != 0) {
            setSpan(
                AbsoluteSizeSpan(context.dimenPx(textSize)),
                startIdx,
                endIdx,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        //Set text color
        setSpan(
            ForegroundColorSpan(context.color(colorId)),
            startIdx,
            endIdx,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        //Set font Family
        if (fontId != 0) {
            setSpan(
                CustomTypefaceSpan(
                    null,
                    Typeface.create(context.font(fontId), Typeface.NORMAL)
                ),
                startIdx,
                endIdx,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        //Set action click
        if (actionClick != null) {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        actionClick.invoke(subText)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                },
                startIdx,
                endIdx,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}

fun TextView.applySpannable(
    spannable: Spannable,
    onBuilder: (Spannable.() -> Unit)? = { setLinkColor(R.color.colorPrimary) }
): TextView {
    onBuilder?.invoke(spannable)
    text = spannable
    return this
}

fun TextView.setLinkColor(@ColorRes colorRes: Int = R.color.colorPrimary) {
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
    setLinkTextColor(context.color(colorRes))
}