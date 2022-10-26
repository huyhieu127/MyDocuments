package com.huyhieu.mydocuments.ui.fragments.fingerprint

import android.graphics.Color
import android.os.Bundle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentFingerprintBinding
import com.huyhieu.mydocuments.utils.extensions.setOnClickMyListener
import com.huyhieu.mydocuments.utils.logDebug

class FingerprintFragment : BaseFragment<FragmentFingerprintBinding>() {
    private val biometricPrompt by lazy {
        createBiometricPrompt(
            onAuthenticationError = { _: Int, _: CharSequence ->
                mBinding.root.setBackgroundColor(Color.YELLOW)
            },
            onAuthenticationFailed = {
                mBinding.root.setBackgroundColor(Color.RED)
            },
            onAuthenticationSucceeded = {
                mBinding.root.setBackgroundColor(Color.GREEN)
            }
        )
    }

    override fun initializeBinding() = FragmentFingerprintBinding.inflate(layoutInflater)

    override fun FragmentFingerprintBinding.addControls(savedInstanceState: Bundle?) {
        imgFingerprint.setOnClickMyListener {
            context.isBiometricAvailable { isAvailable, errorCode ->
                logDebug("isBiometricAvailable : $isAvailable , ")
                showDialogFingerprint(biometricPrompt)
            }
        }
    }

    override fun FragmentFingerprintBinding.addEvents(savedInstanceState: Bundle?) {
    }
}