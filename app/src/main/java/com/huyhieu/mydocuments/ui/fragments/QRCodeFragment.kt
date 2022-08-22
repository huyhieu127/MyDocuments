package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentQrCodeBinding
import com.huyhieu.mydocuments.utils.commons.BarcodeScanner
import com.huyhieu.mydocuments.utils.commons.HoleRectangle


class QRCodeFragment : BaseFragment<FragmentQrCodeBinding>() {
    private var barcode: BarcodeScanner? = null
    override fun initializeBinding() = FragmentQrCodeBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
        mBinding.hvQRCode.holeRectangle = HoleRectangle(
            mBinding.viewScan,
            radius = resources.getDimension(R.dimen.radius),
            padding = 0F
        )
        barcode = BarcodeScanner(this, mBinding.previewView, mBinding.viewScan) {
            mBinding.tvResult.text = it?.rawValue ?: ""
        }
    }

    override fun addEvents(savedInstanceState: Bundle?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        barcode?.shutdown()
    }

}