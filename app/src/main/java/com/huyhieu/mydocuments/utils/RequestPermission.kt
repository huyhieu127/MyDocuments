package com.huyhieu.mydocuments.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


fun Fragment.launchPermission(
    onGranted: (() -> Unit)? = null,
    onDinned: (() -> Unit)? = null
) =
    this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            onGranted?.invoke()
        } else {
            onDinned?.invoke()
        }
    }

// util method
fun Context.hasPermissions(context: Context, vararg permissions: String): Boolean =
    permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }