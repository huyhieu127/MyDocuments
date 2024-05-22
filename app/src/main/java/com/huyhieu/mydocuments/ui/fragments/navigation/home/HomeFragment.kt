package com.huyhieu.mydocuments.ui.fragments.navigation.home

import android.content.Intent
import android.os.Bundle
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.huyhieu.documentssdk.DocSdkInstance
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentHomeBinding
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.libraries.extensions.showToastShort
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.models.ApiMenu
import com.huyhieu.mydocuments.models.ChartMenu
import com.huyhieu.mydocuments.models.FeatureMenu
import com.huyhieu.mydocuments.models.GoogleFeatureMenu
import com.huyhieu.mydocuments.models.HomeMenu
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.models.SystemMenu
import com.huyhieu.mydocuments.models.UIUXMenu
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.directions.MainDirections
import com.huyhieu.mydocuments.navigation.navigate
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.MenuAdapter
import com.huyhieu.mydocuments.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {


    private lateinit var manager: SplitInstallManager
    private val adapterMenu by lazy { MenuAdapter() }
    private var lstMenus = mutableListOf<MenuForm>()

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        mActivity.setDarkColorStatusBar()
        manager = SplitInstallManagerFactory.create(mActivity)
        //showNavigationBottom()
        //setTabNavigationBottom(com.huyhieu.mydocuments.libraries.components.UTab.TAB_HOME)
        homeVM.loadMenuFromAsset()
        setupMenu()
    }

    override fun onResume() {
        manager.registerListener(listener)
        super.onResume()
    }

    override fun onPause() {
        manager.unregisterListener(listener)
        super.onPause()
    }

    override fun onMyLiveData(savedInstanceState: Bundle?) {
        homeVM.dataAssetLiveData.observe {
            it ?: return@observe
            lstMenus = it
            adapterMenu.fillData(lstMenus)
        }
        homeVM.githubCommittedLiveData.observe {
            it ?: return@observe
        }
        homeVM.title.observe {
            it ?: return@observe
            vb.toolbar.title = it
        }

        DocSdkInstance.onData = {
            logDebug("onData: $it")
        }
        DocSdkInstance.subscribeData(requireActivity()) {
            logDebug("subscribeData: $it")
        }
    }

    private fun FragmentHomeBinding.setupMenu() {
        rcvMenu.apply {
            setHasFixedSize(true)
            adapter = adapterMenu
        }

        adapterMenu.itemClick = { menuForm ->
            menuForm.menuDirections()
        }
    }

    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->
        val multiInstall = state.moduleNames().size > 1
        val names = state.moduleNames().joinToString(" - ")
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                //  In order to see this, the application has to be uploaded to the Play Store.
                //displayLoadingState(state, "Downloading $names")
                val max = state.totalBytesToDownload().toInt()
                val progress = state.bytesDownloaded().toInt()
                val msg =
                    ("SplitInstallStateUpdatedListener - DOWNLOADING - $progress/$max - $names")
                logDebug(msg)
            }

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {/*
                  This may occur when attempting to download a sufficiently large module.

                  In order to see this, the application has to be uploaded to the Play Store.
                  Then features can be requested until the confirmation path is triggered.
                 */
                mActivity.startIntentSender(state.resolutionIntent()?.intentSender, null, 0, 0, 0)
                logDebug("SplitInstallStateUpdatedListener - REQUIRES_USER_CONFIRMATION")
            }

            SplitInstallSessionStatus.INSTALLED -> {
                //onSuccessfulLoad(names, launch = !multiInstall)
                openIncludeGraphDynamicFeature()
                showToastShort("INSTALLED")
            }

            SplitInstallSessionStatus.INSTALLING -> {
                val max = state.totalBytesToDownload().toInt()
                val progress = state.bytesDownloaded().toInt()

                val msg =
                    ("SplitInstallStateUpdatedListener - DOWNLOADING - $progress/$max - $names")
                logDebug(msg)
                showToastShort(msg)
            }

            SplitInstallSessionStatus.FAILED -> {
                val msg =
                    ("SplitInstallStateUpdatedListener - INSTALLING - FAILED - Error: ${state.errorCode()} for module ${state.moduleNames()}")
                logDebug(msg)
                showToastShort(msg)
            }
            else -> {}
        }
    }

    private fun openIncludeGraphDynamicFeature() {
        logDebug("SplitInstallStateUpdatedListener - INSTALLED")
        val direction = MainDirections.toHelpCenterGraph
        direction.arguments.also { bundle ->
            bundle.putString(
                Constants.DYNAMIC_FEATURES, "Navigate to fragment of include graph"
            )
        }
        navigate(MyNavHost.Main, direction)
    }

    private fun loadAndLaunchModule(name: String) {
        logDebug("loadAndLaunchModule - START")
        // Skip loading if the module already is installed. Perform success action directly.
        if (manager.installedModules.contains(name)) {
            openIncludeGraphDynamicFeature()
            return
        }
        // Create request to install a feature module by name.
        val request = SplitInstallRequest.newBuilder().addModule(name).build()
        // Load and install the requested feature module.
        manager.startInstall(request)
        logDebug("loadAndLaunchModule - Starting install for $name")
    }

    private fun launchActivity(className: String) {
        Intent().setClassName(mActivity.packageName, className).also {
            startActivity(it)
        }
    }

    private fun MenuForm.menuDirections() {
        when (this.type) {
            HomeMenu.MENU_ABOUT -> PopupNotifyFragment.newInstance()
                .showBottomSheet(parentFragmentManager)

            //CHART
            ChartMenu.MENU_RADAR_CHART -> navigate(MyNavHost.Main, MainDirections.toRadarChart)
            ChartMenu.MENU_CUBIC_CHART -> navigate(MyNavHost.Main, MainDirections.toCubicChart)
            ChartMenu.MENU_LINE_CHART -> navigate(MyNavHost.Main, MainDirections.toLineChart)
            //UI/UX
            UIUXMenu.MENU_PULL_TO_REFRESH -> navigate(
                MyNavHost.Main,
                MainDirections.toSwipeToRefresh
            )

            UIUXMenu.MENU_PIN_CODE -> navigate(MyNavHost.Main, MainDirections.toPinCode)
            UIUXMenu.MENU_GUIDE -> navigate(MyNavHost.Main, MainDirections.toGuide)
            UIUXMenu.MENU_SWIPE_RCV -> navigate(MyNavHost.Main, MainDirections.toSwipeRcv)
            UIUXMenu.MENU_STEP_PAGE -> navigate(MyNavHost.Main, MainDirections.toSteps)
            UIUXMenu.MENU_BANNER_INFINITE -> navigate(
                MyNavHost.Main,
                MainDirections.toBannerInfinite
            )

            UIUXMenu.MENU_INTRODUCE -> navigate(MyNavHost.Main, MainDirections.toIntroduce)
            //API
            ApiMenu.MENU_SDK -> DocSdkInstance.openSdk(mActivity)
            ApiMenu.MENU_MULTIPLE_NETWORK_API -> navigate(
                MyNavHost.Main,
                MainDirections.toMultipleAPI
            )
            //FEATURES
            FeatureMenu.MENU_NOTIFICATION -> {}
            FeatureMenu.MENU_MAP -> navigate(MyNavHost.Main, MainDirections.toMap)
            FeatureMenu.MENU_STATIC_MAP -> navigate(MyNavHost.Main, MainDirections.toStaticMap)
            FeatureMenu.MENU_CONTENT_PROVIDER -> navigate(
                MyNavHost.Main,
                MainDirections.toContentProvider
            )
            //SYSTEM
            SystemMenu.MENU_THEME -> {}
            SystemMenu.MENU_LANGUAGE -> {}
            //GOOGLE FEATURES
            GoogleFeatureMenu.MENU_UPDATE_IN_APP -> navigate(
                MyNavHost.Main,
                MainDirections.toInAppUpdate
            )

            GoogleFeatureMenu.MENU_DYNAMIC_FEATURES_FRAGMENT -> {
                val direction = MainDirections.toHelpCenter
                direction.arguments.also { bundle ->
                    bundle.putString(
                        Constants.DYNAMIC_FEATURES, "Navigate to fragment of dynamic features"
                    )
                    bundle.putBoolean(
                        "isFrg", true
                    )
                }
                navigate(MyNavHost.Main, direction)
            }

            GoogleFeatureMenu.MENU_DYNAMIC_FEATURES_INCLUDE_GRAPH -> loadAndLaunchModule("help_center")
        }
    }
}