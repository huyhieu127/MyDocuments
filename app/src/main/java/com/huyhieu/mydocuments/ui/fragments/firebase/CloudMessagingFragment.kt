package com.huyhieu.mydocuments.ui.fragments.firebase

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCloudMessagingBinding
import com.huyhieu.mydocuments.libraries.utils.logDebug


class CloudMessagingFragment : BaseFragment<FragmentCloudMessagingBinding>() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            doSomething()
        } else {
            directlyAskPermission()
        }
    }
    private val requestIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        logDebug(it.toString())
        askNotificationPermission()
        ///Directly request the permission OR Continue without notifications.
    }

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        val result = arguments?.getString("data")
        vb.tvResult.text = "Cloud Messaging: $result"
        askNotificationPermission()
    }

    private fun doSomething() {
        logDebug("Permission granted")
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        checkPermission(
            onRequestPermission = ::directlyAskPermission,
            onSkip = null,
            onGranted = ::doSomething,
            onDenied = ::directlyAskPermission
        )
    }

    private fun checkPermission(
        onRequestPermission: (() -> Unit),
        onSkip: (() -> Unit)? = null,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
                onGranted()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showAlertWarning(onRequestPermission, onSkip)
            } else {
                onDenied()
            }
        } else {
            if (NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
                doSomething()
            } else {
                directlyAskPermission()
            }
        }

    }

    private fun directlyAskPermission() {
        logDebug("askNotificationPermission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            showAlertWarning(
                onRequestPermission = {
                    val intent = Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", requireContext().packageName, null)
                    }
                    requestIntentLauncher.launch(intent)
                },
                null,
            )
        }

    }

    private fun showAlertWarning(
        onRequestPermission: (() -> Unit),
        onSkip: (() -> Unit)? = null,
    ) {
        AlertDialog.Builder(requireContext()).setTitle("Thông Báo")
            .setMessage("Bạn cần bật thông báo để không bỏ lỡ bất kỳ khuyến mãi nào!")
            .setPositiveButton("OK") { dialog, _ ->
                //Directly request the permission
                onRequestPermission()
            }.setNegativeButton("No, Thanks") { dialog, _ ->
                onSkip?.invoke() ?: dialog?.dismiss()
                //Continue without notifications.
            }
            .show()
    }
}