package com.huyhieu.mydocuments.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri

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

fun Activity?.shareImage(uriToImage: Uri? = null, title: String = "") {
    this ?: return
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uriToImage)
        type = "image/png"
    }
    startActivity(Intent.createChooser(shareIntent, title))
}