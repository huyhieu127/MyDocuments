package com.huyhieu.mydocuments.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.models.YouTubeForm

fun Context.loadMedia(): MutableList<YouTubeForm> {
    val videos = mutableListOf<YouTubeForm>()
    tryCatch {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_MODIFIED,
        )
        val selection = null
        val selectionArgs = null
        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"//ASC

        this.applicationContext.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                val video =
                    YouTubeForm(uri = contentUri, title = name, duration = duration, size = size)
                videos.add(video)
            }
        }
    }
    return videos
}

fun Context.loadThumbnail(uri: Uri, size: Size, signal: CancellationSignal? = null): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.applicationContext.contentResolver.loadThumbnail(uri, size, signal)
        } else {
            MediaStore.Video.Thumbnails.getThumbnail(
                this.applicationContext.contentResolver, ContentUris.parseId(uri),
                MediaStore.Video.Thumbnails.MICRO_KIND,
                null
            )
        }
    } catch (_: Exception) {
        null
    }
}