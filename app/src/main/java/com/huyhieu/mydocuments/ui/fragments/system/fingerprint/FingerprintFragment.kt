package com.huyhieu.mydocuments.ui.fragments.system.fingerprint

import android.graphics.Color
import android.os.Bundle
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentFingerprintBinding
import com.huyhieu.mydocuments.libraries.extensions.setOnClickMyListener

class FingerprintFragment : BaseFragment<FragmentFingerprintBinding>() {
    private val biometricPrompt by lazy {
        createBiometricPrompt(
            onAuthenticationError = { _: Int, _: CharSequence ->
                vb.root.setBackgroundColor(Color.YELLOW)
            },
            onAuthenticationFailed = {
                vb.root.setBackgroundColor(Color.RED)
            },
            onAuthenticationSucceeded = {
                vb.root.setBackgroundColor(Color.GREEN)
            }
        )
    }

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        imgFingerprint.setOnClickMyListener {
            context.isBiometricAvailable { isAvailable, errorCode ->
                logDebug("isBiometricAvailable : $isAvailable , ")
                showDialogFingerprint(biometricPrompt)
            }
        }
    }
}