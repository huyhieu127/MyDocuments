package com.huyhieu.mydocuments.ui.fragments

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentIntentSystemBinding
import com.huyhieu.mydocuments.utils.*
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.extensions.showToastShort
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class IntentSystemFragment : BaseFragment<FragmentIntentSystemBinding>() {

    private var uriPicture: Uri? = null
    private var isShowPicture = false

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    override fun initializeBinding() = FragmentIntentSystemBinding.inflate(layoutInflater)

    private val requestPermission = requestPermissions(
        onGranted = {
            mBinding.capture()
        }, onDined = {
            logDebug("Dined!")
        })

    private fun callRequestPermission() {
        requestPermission.launch(permissions)
    }

    override fun FragmentIntentSystemBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar()
    }

    override fun FragmentIntentSystemBinding.addEvents(savedInstanceState: Bundle?) {
        btnCallSupport.setOnClickListener(this@IntentSystemFragment)
        btnCaptureView.setOnClickListenerCustom(listener = this@IntentSystemFragment)
        btnShare.setOnClickListenerCustom(false, listener = this@IntentSystemFragment)
        imgPicture.setOnClickListener(this@IntentSystemFragment)
    }

    override fun FragmentIntentSystemBinding.onClickViewBinding(v: View) {
        when (v.id) {
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
                    mActivity?.showToastShort("Picture not found!")
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
            mActivity?.captureView(root) {
                if (it.isNull()) {
                    mActivity?.showToastShort("Can't capture!")
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