package com.huyhieu.mydocuments.ui.fragments.components

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.commons.HoleRectangle
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.tint
import com.huyhieu.library.utils.BarcodeUtils
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragmentOld
import com.huyhieu.mydocuments.databinding.FragmentQrCodeBinding
import com.huyhieu.mydocuments.utils.requestActivityResult
import kotlinx.coroutines.launch


class QRCodeFragment : BaseFragmentOld<FragmentQrCodeBinding>() {
    private var barcode: BarcodeUtils? = null
    private var chooseImageLauncher = requestActivityResult {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data
            barcode?.scanUri(uri)
        }
    }

    override fun initializeBinding() = FragmentQrCodeBinding.inflate(layoutInflater)

    override fun FragmentQrCodeBinding.addControls(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(false)
        showNavigationBottom()
        lifecycleScope.launch {
            hvQRCode.holeRectangle = HoleRectangle(
                viewScan,
                radius = resources.getDimension(R.dimen.radius),
                padding = 0F
            )
            hvQRCode.onAreaViewListener = {
                if (it) {
                    imgTopLeft.tint(Color.WHITE)
                    imgTopRight.tint(Color.WHITE)
                    imgBottomLeft.tint(Color.WHITE)
                    imgBottomRight.tint(Color.WHITE)
                } else {
                    imgTopLeft.tint(Color.RED)
                    imgTopRight.tint(Color.RED)
                    imgBottomLeft.tint(Color.RED)
                    imgBottomRight.tint(Color.RED)
                }
            }
            barcode =
                BarcodeUtils(
                    this@QRCodeFragment,
                    previewView,
                    BarcodeUtils.BarcodeType.QR_CODE
                ) { barcode, sizeImage ->
                    barcode?.let {
                        hvQRCode.sizeImage = sizeImage
                        val rect = barcode.boundingBox
                        hvQRCode.rectResult = rect
                        /*val points = barcode.cornerPoints
                        hvQRCode.pointsResult = points*/
                        tvResult.text = it.rawValue
                    } ?: run {
                        hvQRCode.rectResult = null
                        hvQRCode.pointsResult = null
                        tvResult.text = "Not found!"
                    }
                }
        }
    }

    override fun FragmentQrCodeBinding.addEvents(savedInstanceState: Bundle?) {
        imgGallery.setOnClickListener(this@QRCodeFragment)
    }

    override fun FragmentQrCodeBinding.onClickViewBinding(v: View) {
        when (v.id) {
            imgGallery.id -> {
                showIntentChoosePicture()
            }
        }
    }

    private fun showIntentChoosePicture() {
        Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }.run {
            chooseImageLauncher.launch(Intent.createChooser(this, "Chọn hình ảnh chứa QR Code"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        barcode?.shutdown()
    }

}