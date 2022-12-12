package com.huyhieu.mydocuments.ui.fragments.fingerprint

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.huyhieu.library.utils.logDebug

/**
 * Check biometric*/
fun Context?.isBiometricAvailable(
    authenticators: Int = Authenticators.BIOMETRIC_WEAK,
    onStatus: ((isAvailable: Boolean, code: Int) -> Unit)? = null,
) {
    this ?: return
    val biometricManager = BiometricManager.from(this)
    when (biometricManager.canAuthenticate(authenticators)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            onStatus?.invoke(true, BiometricManager.BIOMETRIC_SUCCESS)
        }
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            onStatus?.invoke(false, BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE)
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            //createIntentSettingBiometric()
            onStatus?.invoke(false, BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED)
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            onStatus?.invoke(false, BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE)
        }
        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            onStatus?.invoke(false, BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED)
        }
        else -> {
            onStatus?.invoke(false, biometricManager.canAuthenticate(authenticators))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun createIntentSettingBiometric() = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
    putExtra(
        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
        Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL
    )
}

/**
 * Create instance of BiometricPrompt*/
fun <T> T.createBiometricPrompt(
    onAuthenticationError: ((errorCode: Int, errString: CharSequence) -> Unit)? = null,
    onAuthenticationFailed: (() -> Unit)? = null,
    onAuthenticationSucceeded: ((result: BiometricPrompt.AuthenticationResult) -> Unit)? = null,
): BiometricPrompt? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        when (this) {
            is Fragment -> {
                val executor = ContextCompat.getMainExecutor(this.requireContext())
                BiometricPrompt(
                    this,
                    executor,
                    callBackBiometricPrompt(
                        onAuthenticationError,
                        onAuthenticationFailed,
                        onAuthenticationSucceeded
                    )
                )

            }
            is FragmentActivity -> {
                val executor = ContextCompat.getMainExecutor(this)
                BiometricPrompt(
                    this,
                    executor,
                    callBackBiometricPrompt(
                        onAuthenticationError,
                        onAuthenticationFailed,
                        onAuthenticationSucceeded
                    )
                )
            }
            else -> null
        }
    } else {
        null
    }
}

/**
 * Call back BiometricPrompt.AuthenticationCallback*/
private fun callBackBiometricPrompt(
    onAuthenticationError: ((errorCode: Int, errString: CharSequence) -> Unit)? = null,
    onAuthenticationFailed: (() -> Unit)? = null,
    onAuthenticationSucceeded: ((result: BiometricPrompt.AuthenticationResult) -> Unit)? = null,
): BiometricPrompt.AuthenticationCallback {
    return object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onAuthenticationError?.invoke(errorCode, errString)
            logDebug("onAuthenticationError: $errorCode :: $errString")
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onAuthenticationFailed?.invoke()
            logDebug("onAuthenticationFailed: Authentication failed for an unknown reason")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onAuthenticationSucceeded?.invoke(result)
            logDebug("onAuthenticationSucceeded: Authentication was successful")
        }
    }
}

/**
 * Build PromptInfo*/
private val promptInfo by lazy {
    BiometricPrompt.PromptInfo.Builder()
        .setTitle("Title")
        .setSubtitle("Subtitle")
        .setDescription("Description")
        //.setAllowedAuthenticators(Authenticators.BIOMETRIC_WEAK)
        .setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG)
        //.setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL)
        .setNegativeButtonText("Negative Button Text")
        .build()
}

/**
 * Display the login prompt*/
fun showDialogFingerprint(biometricPrompt: BiometricPrompt?) {
    biometricPrompt
        ?: throw Exception("""Function "showDialogFingerprint": BiometricPrompt is null""")
    biometricPrompt.authenticate(promptInfo)
}
