package com.huyhieu.library

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.huyhieu.library.utils.logDebug
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun Context?.captureView(v: View, onCaptured: ((uri: Uri?) -> Unit)? = null) {
    val bitmap = v.toBitmap()
    if (bitmap != null) {
        savePictureByBitmap(bitmap) {
            onCaptured?.invoke(it)
        }
    } else {
        onCaptured?.invoke(null)
    }
}

fun View.toBitmap(): Bitmap? {
    // create a bitmap object
    var screenshot: Bitmap? = null
    try {
        // inflate screenshot object with Bitmap.createBitmap.
        // It  requires three parameters:
        // width, height of the view, the background color
        screenshot =
            Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)
        // Now draw this bitmap on a canvas
        val canvas = Canvas(screenshot!!)
        this.draw(canvas)
    } catch (e: Exception) {
        logDebug("Failed to capture screenshot because: ${e.message}")
    }
    // return the bitmap
    return screenshot
}


// this method saves the image to gallery
fun Context?.savePictureByBitmap(
    bitmap: Bitmap,
    fileName: String = "Capture_view_${System.currentTimeMillis()}.png",
    mimeType: String = "image/png",
    onSaved: ((uri: Uri?) -> Unit)? = null
) {
    var uriFile: Uri? = null
    val prefixFile = "png"
    // Output stream
    var fos: OutputStream? = null
    // For devices running android >= Q
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // getting the contentResolver
        this?.contentResolver?.also { resolver ->
            // Content resolver will process the content values
            val contentValues = ContentValues().apply {
                // putting file information in content values
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            // Inserting the content Values to
            // contentResolver and getting the Uri
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            // Opening an outputStream with the Uri that we got
            imageUri?.let {
                fos = resolver.openOutputStream(it)
                uriFile = imageUri
                //uriFile = this.getRealPathFromURI(it)?.toUri() ?: it
            }
        }
    } else {
        // These for devices running on android < Q
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, fileName)
        fos = FileOutputStream(image)
        uriFile = Uri.fromFile(image)
    }

    fos?.use {
        // Finally writing the bitmap to the output stream that we opened
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        onSaved?.invoke(uriFile)
    }
}