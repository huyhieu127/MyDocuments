package com.huyhieu.mydocuments.ui.fragments.google_play.in_app_update

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentInAppUpdateBinding
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.utils.toJson

class InAppUpdateFragment : BaseFragment<FragmentInAppUpdateBinding>() {
    private lateinit var appUpdateManager: AppUpdateManager
    private val appUpdateInfoTask get() = appUpdateManager.appUpdateInfo
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            unregisterListenerUpdateFlexible()
            if (result.resultCode != Activity.RESULT_OK) {
                logDebug("Update flow failed! Result code: " + result.resultCode)
                appUpdateManager = AppUpdateManagerFactory.create(requireContext())
            }
        }
    private var isInstallNow = false

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        appUpdateManager = AppUpdateManagerFactory.create(requireContext())
        loadVersionLocal()
        setClickViews(vb.btnCheckVersion)
    }

    private fun loadVersionLocal() {
        vb.tvCurrentVersion.text = BuildConfig.VERSION_NAME
        vb.tvCurrentVersionCode.text = BuildConfig.VERSION_CODE.toString()
    }

    override fun FragmentInAppUpdateBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            btnCheckVersion -> {
                if (isInstallNow) {
                    appUpdateManager.completeUpdate()
                } else {
                    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                        logDebug("appUpdateInfo: ${appUpdateInfo.toJson()}")
                        vb.tvLatestVersionCode.text =
                            appUpdateInfo.availableVersionCode().toString()
                        when {//Check update status is Downloaded
                            appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED || appUpdateInfo.installStatus() == InstallStatus.DOWNLOADING -> {
                                vb.tvLatestVersionCode.text =
                                    appUpdateInfo.availableVersionCode().toString()
                                registerListenerUpdateFlexible()
                            }

                            appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                                AppUpdateType.FLEXIBLE
                            ) -> {
                                registerListenerUpdateFlexible()
                                requestUpdate(appUpdateInfo)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            when {
                //Check update status is Downloaded
                appUpdateInfo.installStatus() == InstallStatus.DOWNLOADING -> {
                    vb.tvLatestVersionCode.text = appUpdateInfo.availableVersionCode().toString()
                    registerListenerUpdateFlexible()
                }

                appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED -> {
                    vb.tvLatestVersionCode.text = appUpdateInfo.availableVersionCode().toString()
                    stateDownloaded()
                }
            }
        }
    }

    private fun requestUpdate(appUpdateInfo: AppUpdateInfo) {
        val build = AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, activityResultLauncher, build)
    }

    /**
     * For AppUpdateType.FLEXIBLE
     * */
    // Create a listener to track request state updates.
    private val listenerFlexible
        get() = InstallStateUpdatedListener { state ->
        // (Optional) Provide a download progress bar.
        when {
            state.installStatus() == InstallStatus.DOWNLOADING -> {
                val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()
                // Show update progress bar.
                vb.btnCheckVersion.setText("${(bytesDownloaded.toMb())}/${totalBytesToDownload.toMb()} MB")
                if (vb.btnCheckVersion.isEnabled) vb.btnCheckVersion.isEnabled = false
            }

            state.installStatus() == InstallStatus.DOWNLOADED -> stateDownloaded()
        }
    }

    private fun stateDownloaded() {
        isInstallNow = true
        vb.btnCheckVersion.setText("Install now!")
        vb.btnCheckVersion.isEnabled = true
    }

    private fun Long.toMb() = String.format("%.2f", (this / (1000000.0)))

    // Before starting an update, register a listener for updates.
    private fun registerListenerUpdateFlexible() {
        appUpdateManager.registerListener(listenerFlexible)
    }

    // .....Start an update.

    // When status updates are no longer needed, unregister the listener.
    private fun unregisterListenerUpdateFlexible() {
        appUpdateManager.unregisterListener(listenerFlexible)
    }
}