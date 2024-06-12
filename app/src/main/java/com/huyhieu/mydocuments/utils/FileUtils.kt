package com.huyhieu.mydocuments.utils

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.os.Environment
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresPermission
import com.huyhieu.data.logger.logDebug
import com.huyhieu.data.logger.logError
import com.huyhieu.mydocuments.App.Companion.ins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

val packageDir: File
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ins.getExternalFilesDir(null)!!
    } else {
        @Suppress("DEPRECATION")
        Environment.getExternalStorageDirectory()
    }

fun Closeable.safeClose() {
    try {
        close()
    } catch (ignored: Exception) {
    }
}

fun readAssets(filename: String): String? {
    return try {
        val sb = StringBuilder()
        BufferedReader(InputStreamReader(ins.assets.open(filename))).useLines { lines ->
            lines.forEach {
                sb.append(it)
            }
        }
        return sb.toString()
    } catch (e: FileNotFoundException) {
        null
    }
}

fun open(file: File) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        ins.startActivity(Intent.createChooser(intent, ""))
    } catch (e: Exception) {
    }
}

fun copyFile(fileName: String) {
    var inputStream: InputStream? = null
    var fos: FileOutputStream? = null
    try {
        inputStream = ins.assets.open(fileName)
        fos = FileOutputStream("${packageDir.absolutePath}/$fileName")
        val buffer = ByteArray(1024)
        var read: Int = inputStream.read(buffer)
        while (read >= 0) {
            fos.write(buffer, 0, read)
            read = inputStream.read(buffer)
        }
    } catch (e: IOException) {
    }
    inputStream?.close()
    fos?.flush()
    fos?.close()
}


fun getFile(fileName: String): File {
    return File(packageDir, fileName)
}

@RequiresPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
fun writeFile(fileName: String, bytes: ByteArray): File? {
    return try {
        val file = File(packageDir, fileName)
        val stream = FileOutputStream(file)
        stream.write(bytes)
        stream.flush()
        stream.close()
        file
    } catch (e: Exception) {
        null
    }
}

@RequiresPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
fun readFile(fileName: String): String {
    val file = File(packageDir, fileName)
    val text = java.lang.StringBuilder()
    try {
        val br = BufferedReader(FileReader(file))
        var line: String?
        while (br.readLine().also { line = it } != null) {
            text.append(line)
            text.append('\n')
        }
        br.close()
    } catch (e: IOException) {
    }
    return text.toString()
}

val File.size: Long get() = length() / 1024

fun File.getThumbnail(): Bitmap? {
    try {
        val file = File(path)
        if (file.absoluteFile.exists()) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ThumbnailUtils.createVideoThumbnail(file, Size(96, 96), CancellationSignal())
            } else {
                ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND)
            }
        }
    } catch (e: Exception) {

    }
    return null
}

fun copyFolder(sourceFile: File, destinationFile: File) {
    logDebug("copy files from '$sourceFile' to '$destinationFile'")
    destinationFile.mkdirs()
    copyFiles(sourceFile, destinationFile)
}

fun copyFiles(sourceFile: File, destinationFile: File) {
    try {
        val folderFiles = sourceFile.list() ?: return
        sourceFile.mkdirs()
        for (file in folderFiles) {
            val childSourceFile = File(sourceFile.path, file)
            val childDestinationFile = File(destinationFile.path, file)
            if (file.contains(".")) {
                logDebug("copying '$file''")
                copyFile(childSourceFile, childDestinationFile)
            } else {
                copyFiles(childSourceFile, childDestinationFile)
            }
        }
    } catch (e: Exception) {
        logError(e.message)
    }
}

fun copyFile(sourceFile: File, destinationFile: File) {
    var inputStream: InputStream? = null
    var fos: FileOutputStream? = null
    try {
        // create destination file
        if (destinationFile.absoluteFile.exists()) {
            destinationFile.delete()
        }
        destinationFile.createNewFile()

        // write buffer
        inputStream = sourceFile.inputStream()
        fos = FileOutputStream(destinationFile)
        val buffer = ByteArray(1024)
        var read: Int = inputStream.read(buffer)
        while (read >= 0) {
            fos.write(buffer, 0, read)
            read = inputStream.read(buffer)
        }
    } catch (e: IOException) {
        logError("copy file error: ${e.message}")
    }
    inputStream?.safeClose()
    fos?.flush()
    fos?.safeClose()
}

fun CoroutineScope.unzip(
    zipFile: File,
    targetDirectory: File,
    onCompleted: suspend CoroutineScope.() -> Unit
) {
    launch(Dispatchers.IO) {
        try {
            ZipInputStream(BufferedInputStream(FileInputStream(zipFile))).use { zipInputStream ->
                try {
                    var ze: ZipEntry? = null
                    var count: Int
                    val buffer = ByteArray(8192)
                    while (zipInputStream.nextEntry.also { ze = it } != null) {
                        val zipEntry = ze ?: break
                        val file = File(targetDirectory, zipEntry.name)
                        val dir = if (zipEntry.isDirectory) file else file.parentFile
                        if (!dir.isDirectory && !dir.mkdirs()) throw FileNotFoundException(
                            "Failed to ensure directory: " +
                                    dir.absolutePath
                        )
                        if (zipEntry.isDirectory) continue
                        val fileOutputStream = FileOutputStream(file)
                        fileOutputStream.use { fileOut ->
                            while (zipInputStream.read(buffer)
                                    .also { count = it } != -1
                            ) fileOut.write(
                                buffer,
                                0,
                                count
                            )
                        }
                    }
                    zipInputStream.safeClose()
                } catch (e: Exception) {
                    zipInputStream.safeClose()
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            logError(e.message)
        }
        this.launch(Dispatchers.IO, CoroutineStart.DEFAULT, onCompleted)
    }
}

fun Context?.getRealPathFromURI(contentUri: Uri?): String? {
    this ?: return null
    contentUri ?: return null
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = contentResolver.query(contentUri, proj, null, null, null)

        cursor ?: return null

        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        cursor.getString(columnIndex)
    } finally {
        cursor?.close()
    }
}