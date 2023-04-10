package com.huyhieu.library.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.util.TypedValue


fun Float.spToPx(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics)
        .toInt()

fun Int.spToPx(context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        context.resources.displayMetrics
    )
        .toInt()

fun Float.dpToPx(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
        .toInt()

fun Int.dpToPx(context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )
        .toInt()

fun Float.pxToDp(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this, context.resources.displayMetrics)
        .toInt()

fun Int.pxToDp(context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this.toFloat(),
        context.resources.displayMetrics
    )
        .toInt()

fun Float.dpToSp(context: Context) =
    (dpToPx(context) / context.resources.displayMetrics.scaledDensity)

fun Int.dpToSp(context: Context) =
    (dpToPx(context) / context.resources.displayMetrics.scaledDensity)

fun Any?.isNull() = this == null

fun Activity?.callPhoneNumber(phone: String = "123456789") {
    this ?: return
    val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    startActivity(intentDial)
}

fun Activity?.shareMessage(msg: String = "") {
    this ?: return
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, msg)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Context?.copyText(text: String?) {
    this ?: return
    val clipboard: ClipboardManager? =
        this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText("label", text ?: "")
    clipboard?.setPrimaryClip(clip)
}

fun Activity?.shareImage(uriToImage: Uri? = null, title: String = "") {
    this ?: return
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uriToImage)
        type = "image/png"
    }
    startActivity(Intent.createChooser(shareIntent, title))
}