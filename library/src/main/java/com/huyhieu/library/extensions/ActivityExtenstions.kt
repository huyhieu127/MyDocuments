package com.huyhieu.library.extensions

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.WindowMetrics
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat

fun Activity.isAppRunning(packageName: String): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val procInfos = activityManager.runningAppProcesses
    if (procInfos != null) {
        for (processInfo in procInfos) {
            if (processInfo.processName == packageName) {
                return true
            }
        }
    }
    return false
}

fun Activity.requestPermissionsApp(lstPermission: Array<String>, requestCode: Int) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(this, lstPermission, requestCode)
    }
}


fun Activity?.hideKeyboard() {
    this?.let {
        val imm: InputMethodManager =
            it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = it.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity?.setDarkColorStatusBar(isDark: Boolean = true) {
    this?.window?.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = Color.TRANSPARENT
        if (isDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                this.apply {
                    statusBarColor = Color.BLACK
                }
            }
        }
    }
}

fun Activity?.setTransparentStatusBarNew(
    isTransparent: Boolean = true,
    colorStatusBar: Int = Color.WHITE
) {
    this ?: return
    val window = this.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (isTransparent) {
        window.statusBarColor = Color.TRANSPARENT
        WindowCompat.setDecorFitsSystemWindows(window, false)
    } else {
        window.statusBarColor = colorStatusBar
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }
}

fun Activity?.setTransparentStatusBar(
    isTransparent: Boolean = true,
    @ColorRes idColor: Int = android.R.color.white
) {
    this ?: return
    when (isTransparent) {
        true -> {
            val w: Window = window
            w.statusBarColor = color(android.R.color.transparent)
            this.setNavigationBarColor(android.R.color.transparent)
        }
        false -> {
            val w: Window = window
            w.statusBarColor = color(idColor)
            this.setNavigationBarColor(idColor)
        }
    }
}

fun Activity?.setNavigationBarColor(@ColorRes idColor: Int = android.R.color.white) {
    this ?: return
    val w: Window = window
    w.navigationBarColor = color(idColor)
}

fun Activity?.setFullScreen() {
    this?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this?.window?.attributes?.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}

fun Activity?.widthApp(): Int {
    return this?.resources?.displayMetrics?.widthPixels ?: 0
}

fun Activity?.heightApp(): Int {
    return this?.resources?.displayMetrics?.heightPixels ?: 0
}

fun Activity?.heightScreen(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        heightScreenFromAPI30R()
    } else {
        val display = this!!.windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        if (size.y > 0) {
            size.y
        } else {
            heightApp()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun Activity?.heightScreenFromAPI30R(): Int {
    val windowMetrics: WindowMetrics? = this?.windowManager?.currentWindowMetrics
    return windowMetrics?.bounds?.height() ?: heightApp()
}

fun Activity.openCHPlay() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}