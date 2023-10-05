package com.huyhieu.mydocuments.ui.fragments.google_play.in_app_update

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentInAppUpdateBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.utils.toJson

class InAppUpdateFragment : BaseFragment<FragmentInAppUpdateBinding>() {
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(requireContext()) }
    private val appUpdateInfoTask by lazy { appUpdateManager.appUpdateInfo }
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode != Activity.RESULT_OK) {
                logDebug("Update flow failed! Result code: " + result.resultCode)
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
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
                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                    logDebug("appUpdateInfo: ${appUpdateInfo.toJson()}")
                    when {
                        //Check update status is Downloaded
                        appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED -> {
                            //Use for AppUpdateType.FLEXIBLE
                            popupSnackbarForCompleteUpdate()
                        }

                        appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) //AppUpdateType.FLEXIBLE
                            //Check if the version is too old when using AppUpdateType.FLEXIBLE
                            //&& (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE
                        -> {
                            // beforeUpdateFlexible() //AppUpdateType.FLEXIBLE
                            // Request the update.
                            logDebug("appUpdateInfo Available: ${appUpdateInfo.toJson()}")
                            requestUpdate(appUpdateInfo)
                        }
                    }
                }
            }
        }
    }

    private fun requestUpdate(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            activityResultLauncher,
            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
            // flexible updates.
            AppUpdateOptions
                .newBuilder(AppUpdateType.IMMEDIATE)
                .build()
        )
    }

    /**
     * For AppUpdateType.FLEXIBLE
     * */
    // Create a listener to track request state updates.
    private val listenerFlexible = InstallStateUpdatedListener { state ->
        // (Optional) Provide a download progress bar.
        when {
            state.installStatus() == InstallStatus.DOWNLOADING -> {
                val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()
                // Show update progress bar.
            }

            state.installStatus() == InstallStatus.DOWNLOADED -> {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupSnackbarForCompleteUpdate()
            }
        }
        // Log state or install the update.
    }

    // Before starting an update, register a listener for updates.
    private fun beforeUpdateFlexible() {
        appUpdateManager.registerListener(listenerFlexible)
    }

    // .....Start an update.

    // When status updates are no longer needed, unregister the listener.
    private fun cancelUpdateFlexible() {
        appUpdateManager.unregisterListener(listenerFlexible)
    }

    // Displays the snackbar notification and call to action.
    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            vb.root,
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") {
                appUpdateManager.completeUpdate()
            }
            setActionTextColor(context.color(R.color.colorPrimary))
            show()
        }
    }
}