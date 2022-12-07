package com.huyhieu.widget.utils

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.media.Image
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.huyhieu.mydocuments.utils.logDebug
import com.huyhieu.mydocuments.utils.requestPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class BarcodeUtils(
    private val fragment: Fragment,
    private val previewView: PreviewView,
    typeScan: BarcodeType,
    private val onResult: (Barcode?, sizeImage: Point?) -> Unit
) {
    enum class BarcodeType {
        QR_CODE,
        FACE_DETECT
    }

    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private val requestPermissionsLauncher: ActivityResultLauncher<Array<String>> = fragment.run {
        return@run requestPermissions(
            onGranted = {
                startCamera()
            },
            onDined = {
                showDialogRequest()
            })
    }


    // Process image searching for barcodes
    private var options: BarcodeScannerOptions? = null

    companion object {
        var PERMISSION_CAMERA = Manifest.permission.CAMERA
    }


    init {
        options = when (typeScan) {
            BarcodeType.QR_CODE -> {
                BarcodeScannerOptions.Builder()
                    //.setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
            }
            BarcodeType.FACE_DETECT -> {
                null
            }
            else -> null
        }
        requestPermissionsLauncher.launch(arrayOf(PERMISSION_CAMERA))
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(fragment.requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(fragment.requireContext()))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {
        cameraProvider ?: return
        // Preview
        val preview = Preview.Builder().build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        // Image analyzer
        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .build()
            .also {
                it.setAnalyzer(
                    cameraExecutor, QrCodeAnalyzer()
                )
            }

        // Select back camera as a default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()
            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                fragment.viewLifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )

        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    private fun showDialogRequest() {
        MaterialAlertDialogBuilder(fragment.requireContext())
            .setTitle("Permission required")
            .setMessage("This application needs to access the camera to process barcodes")
            .setPositiveButton("Ok") { _, _ ->
                // Keep asking for permission until granted
                requestPermissionsLauncher.launch(arrayOf(PERMISSION_CAMERA))
            }
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }

    fun shutdown() {
        cameraExecutor.shutdown()
    }

    fun scanImage(inputImage: InputImage, onComplete: (() -> Unit)? = null) {
        options?.let { options ->
            val scanner = BarcodeScanning.getClient(options)
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        for (barcode in barcodes) {
                            // Handle received barcodes...
                            when (barcode.format) {
                                Barcode.FORMAT_QR_CODE -> {
                                }
                            }
                            onResult.invoke(barcode, Point(inputImage.height, inputImage.width))
                        }
                    } else {
                        onResult.invoke(null, null)
                    }
                }
                .addOnFailureListener { ex ->
                    val error = ex.message
                    logDebug(error)
                }
                .addOnCompleteListener {
                    onComplete?.invoke()
                }
        }
    }

    fun scanUri(uri: Uri?) {
        uri ?: return
        try {
            val image = InputImage.fromFilePath(fragment.requireContext(), uri)
            CoroutineScope(Dispatchers.IO).launch {
                //onResult.invoke("Scanning...")
                scanImage(image)
            }
        } catch (ex: Exception) {
            logDebug(ex.message)
        }
    }

    fun Uri.toBitmap(): Bitmap {
        val bitmap = BitmapFactory.decodeStream(
            this@BarcodeUtils.fragment.requireContext().contentResolver.openInputStream(this)
        )
        val blob = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 100, blob)
        return bitmap
    }

    private inner class QrCodeAnalyzer : ImageAnalysis.Analyzer {

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val img = imageProxy.image
            if (img != null) {
                //val bitmap = imageProxy.image!!.toBitmap()
                val inputImage =
                    InputImage.fromMediaImage(img, imageProxy.imageInfo.rotationDegrees)

                //Scan
                scanImage(inputImage) {
                    imageProxy.close()
                    logDebug("Complete!${inputImage.height} - ${inputImage.width}")
                }

            }
        }

        fun Image.toBitmap(): Bitmap {
            val yBuffer = planes[0].buffer // Y
            val vuBuffer = planes[2].buffer // VU

            val ySize = yBuffer.remaining()
            val vuSize = vuBuffer.remaining()

            val nv21 = ByteArray(ySize + vuSize)

            yBuffer.get(nv21, 0, ySize)
            vuBuffer.get(nv21, ySize, vuSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
            val imageBytes = out.toByteArray()
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            val matrix = Matrix()
            matrix.postRotate(90F)
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}
