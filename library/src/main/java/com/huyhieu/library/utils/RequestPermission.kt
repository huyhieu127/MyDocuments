package com.huyhieu.library.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


fun Fragment.requestActivityResult(onResult: (result: ActivityResult) -> Unit) =
    this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onResult.invoke(result)
    }

fun Fragment.requestPermissions(
    onGranted: (() -> Unit)? = null,
    onDined: (() -> Unit)? = null
) =
    this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            onGranted?.invoke()
        } else {
            onDined?.invoke()
        }
    }

// util method
fun Context.hasPermissions(vararg permissions: String): Boolean =
    permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }