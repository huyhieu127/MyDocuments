package com.huyhieu.mydocuments.utils.commons

import android.Manifest
import android.annotation.SuppressLint
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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeScanner(
    private val fragment: Fragment,
    private val previewView: PreviewView,
    private val onResult: (Any) -> Unit
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

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // Image analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrCodeAnalyzer(onResult))
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    fragment.viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(fragment.requireContext()))
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

    private class QrCodeAnalyzer(val onResult: (Any) -> Unit) : ImageAnalysis.Analyzer {

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val inputImage =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // Process image searching for barcodes
                val scanner = BarcodeScanning.getClient(options)

                scanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            // Handle received barcodes...
                            onResult.invoke(barcode)
                        }
                    }
                    .addOnFailureListener { }
            }
            mediaImage?.close()
        }
    }
}
