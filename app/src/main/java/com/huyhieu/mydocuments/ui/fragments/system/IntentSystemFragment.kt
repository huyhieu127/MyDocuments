package com.huyhieu.mydocuments.ui.fragments.system

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.utils.callPhoneNumber
import com.huyhieu.library.utils.captureView
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.showToastShort
import com.huyhieu.library.utils.isNull
import com.huyhieu.library.utils.shareImage
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentIntentSystemBinding
import com.huyhieu.library.utils.requestPermissions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class IntentSystemFragment : BaseFragment<FragmentIntentSystemBinding>() {

    private var uriPicture: Uri? = null
    private var isShowPicture = false

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    private val requestPermission = requestPermissions(
        onGranted = {
            vb.capture()
        }, onDined = {
            logDebug("Dined! - WRITE_EXTERNAL_STORAGE or READ_EXTERNAL_STORAGE")
        })

    private fun callRequestPermission() {
        requestPermission.launch(permissions)
    }

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        mActivity.setDarkColorStatusBar()
        setViewsClick(btnCallSupport, btnCaptureView, imgPicture)
        btnShare.setOnClickListenerCustom(false, listener = this@IntentSystemFragment)
    }


    override fun FragmentIntentSystemBinding.onClickViewBinding(v: View, id: Int) {
        when (id) {
            btnCallSupport.id -> {
                lifecycleScope.launch {
                    delay(2000)
                    btnCallSupport.hideLoading()
                    mActivity.callPhoneNumber()
                }
            }
            btnCaptureView.id -> {
                callRequestPermission()
            }
            btnShare.id -> {
                if (uriPicture != null) {
                    mActivity.shareImage(uriPicture)
                } else {
                    mActivity.showToastShort("Picture not found!")
                }
            }
            imgPicture.id -> {
                /*isShowPicture = true
                if (!imgPicture.isVisible) {
                    imgPicture.isVisible = true
                }*/
            }
        }
    }

    private fun FragmentIntentSystemBinding.capture() {
        lifecycleScope.launch {
            // Wait view init
            delay(1000)
            btnCaptureView.hideLoading()
            // Wait done anim
            delay(500)
            mActivity.captureView(root) {
                if (it.isNull()) {
                    mActivity.showToastShort("Can't capture!")
                } else {
                    uriPicture = it
                    imgPicture.setImageURI(it)
                    imgPicture.isVisible = true
                    lifecycleScope.launch {
                        //Time hide picture
                        delay(3000)
                        if (!isShowPicture) {
                            imgPicture.isVisible = false
                        }
                    }
                }
            }
        }
    }
}