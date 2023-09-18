package com.huyhieu.mydocuments.ui.fragments.device

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Pair
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.mlkit.vision.demo.kotlin.textdetector.TextRecognitionProcessor
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentLoggerImeBinding
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.ui.fragments.device.ml_kit_utils.BitmapUtils
import com.huyhieu.mydocuments.ui.fragments.device.ml_kit_utils.VisionImageProcessor
import com.huyhieu.mydocuments.utils.init
import java.io.IOException


@RequiresApi(Build.VERSION_CODES.O)
class LoggerIMEFragment : BaseFragment<FragmentLoggerImeBinding>() {

    private val adapter by lazy { LoggerIMEAdapter() }

    private var imageUri: Uri? = null
    private var selectedSize: String? = SIZE_SCREEN
    private var isLandScape = false

    // Max width (portrait mode)
    private var imageMaxWidth = 0

    // Max height (portrait mode)
    private var imageMaxHeight = 0
    private var imageProcessor: VisionImageProcessor? = null

    private val launchLocal =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // In this case, imageUri is returned by the chooser, save it.
            imageUri = it.data?.data
            tryReloadAndDetectInImage()
        }

    private val launchCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            tryReloadAndDetectInImage()
        }

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        vb.rcvInfo.init(adapter)
        val listInfo = mutableListOf(
            DeviceInfo(title = "App ID", info = getAndroidID()),
            DeviceInfo(title = "App ID", info = "AppID")
        )
        adapter.fillData(listInfo)
        setClickViews(vb.btnCamera, vb.btnLocal)

        vb.imgPreview.post {
            imageMaxWidth = vb.imgPreview.width
            imageMaxHeight = vb.imgPreview.height
        }
    }

    override fun FragmentLoggerImeBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            vb.btnCamera -> startCameraIntentForResult()
            vb.btnLocal -> startChooseImageIntentForResult()
        }
    }


    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createImageProcessor()
        tryReloadAndDetectInImage()
    }

    private fun createImageProcessor() {
        imageProcessor =
            TextRecognitionProcessor(requireContext(), TextRecognizerOptions.Builder().build())
    }

    public override fun onPause() {
        super.onPause()
        imageProcessor?.run { this.stop() }
    }

    public override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.run { this.stop() }
    }

    private fun startChooseImageIntentForResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        launchLocal.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private fun startCameraIntentForResult() =
        with(requireContext()) { // Clean up last time's image
            imageUri = null
            vb.imgPreview.setImageBitmap(null)
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
                imageUri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                launchCamera.launch(takePictureIntent)
            }
        }

    private fun tryReloadAndDetectInImage() {
        logDebug("Try reload and detect image")
        try {
            if (imageUri == null) {
                return
            }

            if (SIZE_SCREEN == selectedSize && imageMaxWidth == 0) {
                // UI layout has not finished yet, will reload once it's ready.
                return
            }

            val imageBitmap =
                BitmapUtils.getBitmapFromContentUri(requireActivity().contentResolver, imageUri)
                    ?: return
            // Clear the overlay first
            vb.graphicOverlay.clear()

            val resizedBitmap: Bitmap
            resizedBitmap =
                if (selectedSize == SIZE_ORIGINAL) {
                    imageBitmap
                } else {
                    // Get the dimensions of the image view
                    val targetedSize: Pair<Int, Int> = targetedWidthHeight

                    // Determine how much to scale down the image
                    val scaleFactor =
                        Math.max(
                            imageBitmap.width.toFloat() / targetedSize.first.toFloat(),
                            imageBitmap.height.toFloat() / targetedSize.second.toFloat()
                        )
                    Bitmap.createScaledBitmap(
                        imageBitmap,
                        (imageBitmap.width / scaleFactor).toInt(),
                        (imageBitmap.height / scaleFactor).toInt(),
                        true
                    )
                }

            vb.imgPreview.setImageBitmap(resizedBitmap)
            if (imageProcessor != null) {
                vb.graphicOverlay.setImageSourceInfo(
                    resizedBitmap.width,
                    resizedBitmap.height,
                    /* isFlipped= */ false
                )
                imageProcessor!!.processBitmap(resizedBitmap, vb.graphicOverlay)
            } else {
                Log.e(
                    TAG,
                    "Null imageProcessor, please check adb logs for imageProcessor creation error"
                )
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error retrieving saved image")
            imageUri = null
        }
    }

    private val targetedWidthHeight: Pair<Int, Int>
        get() {
            val targetWidth: Int
            val targetHeight: Int
            when (selectedSize) {
                SIZE_SCREEN -> {
                    targetWidth = imageMaxWidth
                    targetHeight = imageMaxHeight
                }

                SIZE_640_480 -> {
                    targetWidth = if (isLandScape) 640 else 480
                    targetHeight = if (isLandScape) 480 else 640
                }

                SIZE_1024_768 -> {
                    targetWidth = if (isLandScape) 1024 else 768
                    targetHeight = if (isLandScape) 768 else 1024
                }

                else -> throw IllegalStateException("Unknown size")
            }
            return Pair(targetWidth, targetHeight)
        }

    private fun getAndroidID(): String {
        return Settings.Secure.getString(
            requireActivity().contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }


    companion object {
        private const val TAG = "StillImageActivity"
        private const val OBJECT_DETECTION = "Object Detection"
        private const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection"
        private const val CUSTOM_AUTOML_OBJECT_DETECTION = "Custom AutoML Object Detection (Flower)"
        private const val FACE_DETECTION = "Face Detection"
        private const val BARCODE_SCANNING = "Barcode Scanning"
        private const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin"
        private const val TEXT_RECOGNITION_CHINESE = "Text Recognition Chinese"
        private const val TEXT_RECOGNITION_DEVANAGARI = "Text Recognition Devanagari"
        private const val TEXT_RECOGNITION_JAPANESE = "Text Recognition Japanese"
        private const val TEXT_RECOGNITION_KOREAN = "Text Recognition Korean"
        private const val IMAGE_LABELING = "Image Labeling"
        private const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
        private const val CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)"
        private const val POSE_DETECTION = "Pose Detection"
        private const val SELFIE_SEGMENTATION = "Selfie Segmentation"
        private const val FACE_MESH_DETECTION = "Face Mesh Detection (Beta)"

        private const val SIZE_SCREEN = "w:screen" // Match screen width
        private const val SIZE_1024_768 = "w:1024" // ~1024*768 in a normal ratio
        private const val SIZE_640_480 = "w:640" // ~640*480 in a normal ratio
        private const val SIZE_ORIGINAL = "w:original" // Original image size
        private const val KEY_IMAGE_URI = "com.google.mlkit.vision.demo.KEY_IMAGE_URI"
        private const val KEY_IMAGE_MAX_WIDTH = "com.google.mlkit.vision.demo.KEY_IMAGE_MAX_WIDTH"
        private const val KEY_IMAGE_MAX_HEIGHT = "com.google.mlkit.vision.demo.KEY_IMAGE_MAX_HEIGHT"
        private const val KEY_SELECTED_SIZE = "com.google.mlkit.vision.demo.KEY_SELECTED_SIZE"
        private const val REQUEST_IMAGE_CAPTURE = 1001
        private const val REQUEST_CHOOSE_IMAGE = 1002
    }
}