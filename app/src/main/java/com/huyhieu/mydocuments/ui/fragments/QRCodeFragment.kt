package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentQrCodeBinding
import com.huyhieu.mydocuments.utils.commons.BarcodeScanner
import com.huyhieu.mydocuments.utils.launchPermission
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class QRCodeFragment : BaseFragment<FragmentQrCodeBinding>() {
    private lateinit var cameraExecutor: ExecutorService
    private var barcode: BarcodeScanner? = null
    private var requestPermission = launchPermission(
        onGranted = {

        },
        onDinned = {
            barcode?.showDialogRequest()
        }
    )


    override fun initializeBinding() = FragmentQrCodeBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
        cameraExecutor = Executors.newSingleThreadExecutor()
        mActivity?.let {
            barcode = BarcodeScanner(it, viewLifecycleOwner, mBinding.previewView, cameraExecutor) {
                requestPermission.launch(arrayOf(BarcodeScanner.PERMISSION_CAMERA))
            }

        }
    }

    override fun addEvents(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

}