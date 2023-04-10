package com.huyhieu.library.extensions

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
import com.huyhieu.library.utils.CustomTypefaceSpan
import com.huyhieu.library.R

/**
 * Multiple style text*/
fun TextView.setSpannable(
    content: String,
    subText: String,
    @DimenRes textSize: Int = 0,
    @FontRes fontId: Int = 0,//R.font.font_svn_gilroy_semibold
    @ColorRes colorId: Int = R.color.colorPrimary,
    actionClick: (() -> Unit)? = null
) {
    /*Set style*/
    val spannable = SpannableString(content).toSpannable(
        context = context,
        subText = subText,
        textSize = textSize,
        fontId = fontId,
        colorId = colorId,
        actionClick = actionClick
    )
    applySpannable(spannable)
}

fun String.toSpannable(
    context: Context,
    subText: String,
    @DimenRes textSize: Int = 0,
    @FontRes fontId: Int = 0,//R.font.font_svn_gilroy_semibold
    @ColorRes colorId: Int = R.color.colorPrimary,
    actionClick: (() -> Unit)? = null
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
    actionClick: (() -> Unit)? = null
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
                        actionClick.invoke()
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
) {
    onBuilder?.invoke(spannable)
    text = spannable
}

fun TextView.setLinkColor(@ColorRes colorRes: Int = R.color.colorPrimary) {
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
    setLinkTextColor(context.color(colorRes))
}