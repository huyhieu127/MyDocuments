package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentQrCodeBinding
import com.huyhieu.mydocuments.utils.commons.BarcodeScanner
import com.huyhieu.mydocuments.utils.commons.HoleRectangle
import com.huyhieu.mydocuments.utils.extensions.showToastShort


class QRCodeFragment : BaseFragment<FragmentQrCodeBinding>() {
    private var barcode: BarcodeScanner? = null
    override fun initializeBinding() = FragmentQrCodeBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
        mBinding.hvQRCode.holeRectangle = HoleRectangle(
            mBinding.vContent,
            radius = resources.getDimension(R.dimen.radius),
            padding = 0F
        )
        barcode = BarcodeScanner(this, mBinding.previewView) {
            mActivity?.showToastShort(it.toString())
        }
    }

    override fun addEvents(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        barcode?.shutdown()
    }

}