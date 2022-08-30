package com.huyhieu.mydocuments.utils

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.utils.extensions.color

fun String.toHyperText(
    subText: String,
    isBold: Boolean = true,
    actionClick: (() -> Unit)? = null
): SpannableString {
    if (this.isEmpty() || subText.isEmpty()) return SpannableString(this)
    val startIdx = this.indexOf(subText)
    val endIdx = startIdx + subText.length
    /*Set style*/
    return SpannableString(this).setSpannableHyperText(
        isBold = isBold,
        startIdx = startIdx,
        endIdx = endIdx,
        actionClick = actionClick
    )
}

fun SpannableString.toHyperText(
    subText: String,
    isBold: Boolean = true,
    actionClick: (() -> Unit)? = null
): SpannableString {
    if (this.isEmpty() || subText.isEmpty()) return this
    val startIdx = this.indexOf(subText)
    val endIdx = startIdx + subText.length
    /*Set style*/
    this.setSpannableHyperText(
        isBold = isBold,
        startIdx = startIdx,
        endIdx = endIdx,
        actionClick = actionClick
    )
    return this
}

private fun SpannableString.setSpannableHyperText(
    isBold: Boolean = true,
    startIdx: Int,
    endIdx: Int,
    actionClick: (() -> Unit)?
): SpannableString {
    apply {
        setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    actionClick?.invoke()
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
        if (isBold) {
            setSpan(
                StyleSpan(Typeface.BOLD),
                startIdx,
                endIdx,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return this
    }
}

fun TextView.setSpannableHyperText(spannable: Spannable) {
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
    setLinkTextColor(context.color(R.color.colorPrimary))
    text = spannable
}