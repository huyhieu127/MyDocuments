package com.huyhieu.mydocuments.utils.extensions

import android.content.Context
import android.widget.Toast

fun Context?.showToastShort(msg: String?) {
    if (this == null) return
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}