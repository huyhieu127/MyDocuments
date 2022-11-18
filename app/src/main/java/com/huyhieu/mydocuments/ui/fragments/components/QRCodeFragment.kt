package com.huyhieu.mydocuments.ui.fragments.components

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentQrCodeBinding
import com.huyhieu.mydocuments.utils.requestActivityResult
import com.huyhieu.widget.utils.BarcodeUtils
import kotlinx.coroutines.launch


class QRCodeFragment : BaseFragment<FragmentQrCodeBinding>() {
    private var barcode: BarcodeUtils? = null
    private var chooseImageLauncher = requestActivityResult {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data
            barcode?.scanUri(uri)
        }
    }

    override fun initializeBinding() = FragmentQrCodeBinding.inflate(layoutInflater)

    override fun FragmentQrCodeBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar(false)
        showNavigationBottom()
        lifecycleScope.launch {
            hvQRCode.holeRectangle = com.huyhieu.widget.commons.HoleRectangle(
                viewScan,
                radius = resources.getDimension(R.dimen.radius),
                padding = 0F
            )
            barcode =
                BarcodeUtils(this@QRCodeFragment, previewView, BarcodeUtils.BarcodeType.QR_CODE) {
                    tvResult.text = it ?: ""
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