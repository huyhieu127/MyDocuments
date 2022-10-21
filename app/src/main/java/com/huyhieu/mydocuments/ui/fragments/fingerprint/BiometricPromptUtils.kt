package com.huyhieu.mydocuments.ui.fragments.fingerprint

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.huyhieu.mydocuments.utils.logDebug

// Since we are using the same methods in more than one Activity, better give them their own file.
object BiometricPromptUtils {
    private const val TAG = "BiometricPromptUtils"


    fun checkDeviceSupport(
        context: Context,
        authenticators: Int = Authenticators.BIOMETRIC_WEAK,
        onNotSupport: (() -> Unit)? = null,
        onSupport: (() -> Unit)? = null,
    ) {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                logDebug("checkDeviceSupport - OK")
                onSupport?.invoke()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                logDebug("checkDeviceSupport - BIOMETRIC_ERROR_HW_UNAVAILABLE")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                logDebug("checkDeviceSupport - BIOMETRIC_ERROR_NONE_ENROLLED")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                logDebug("checkDeviceSupport - BIOMETRIC_ERROR_NO_HARDWARE")
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                logDebug("checkDeviceSupport - BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED")
            }
            else -> {
                logDebug("checkDeviceSupport - FAILED")
                onNotSupport?.invoke()
            }
        }
    }

    fun Fragment.instanceOfBiometricPrompt(): BiometricPrompt? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val executor = ContextCompat.getMainExecutor(requireContext())

            val callback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    logDebug("$errorCode :: $errString")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    logDebug("Authentication failed for an unknown reason")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    logDebug("Authentication was successful")
                }
            }
            return BiometricPrompt(this, executor, callback)
        } else {
            return null
        }
    }

    fun getPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("My App's Authentication")
            .setSubtitle("Please login to get access")
            .setDescription("My App is using Android biometric authentication")
            .setAllowedAuthenticators(Authenticators.BIOMETRIC_WEAK)
            .setNegativeButtonText("setNegativeButtonText")
            .build()
    }
}
