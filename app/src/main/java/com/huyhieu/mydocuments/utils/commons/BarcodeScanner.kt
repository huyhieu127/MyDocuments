package com.huyhieu.mydocuments.utils.commons

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.*
import android.media.Image
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.huyhieu.mydocuments.utils.requestPermissions
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeScanner(
    private val fragment: Fragment,
    private val previewView: PreviewView,
    private val viewScan: View,
    private val onResult: (Barcode?) -> Unit
) {

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

    companion object {
        var PERMISSION_CAMERA = Manifest.permission.CAMERA
    }


    init {
        requestPermissionsLauncher.launch(arrayOf(PERMISSION_CAMERA))
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(fragment.requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(fragment.requireContext()))
    }

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
            //.setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .build()
            .also {
                it.setAnalyzer(
                    cameraExecutor, QrCodeAnalyzer(viewScan, onResult)
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

    private class QrCodeAnalyzer(val viewScan: View, val onResult: (Barcode?) -> Unit) : ImageAnalysis.Analyzer {

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val img = imageProxy.image
            if (img != null) {
                val bitmap = imageProxy.image!!.toBitmap()
                val inputImage =
                    InputImage.fromMediaImage(img, imageProxy.imageInfo.rotationDegrees)

                // Process image searching for barcodes
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    /*.setBarcodeFormats(Barcode.TYPE_URL)*/
                    .build()

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
                                onResult.invoke(barcode)
                            }
                        } else {
                            onResult.invoke(null)
                        }
                    }
                    .addOnFailureListener {
                        val ex = it.message
                        //onResult.invoke("Error: $ex")
                    }
                    .addOnSuccessListener {
                        imageProxy.close()
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
            val bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            return bitmapRotated
        }
    }


}
