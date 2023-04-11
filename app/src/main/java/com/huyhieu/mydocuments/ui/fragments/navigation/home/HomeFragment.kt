package com.huyhieu.mydocuments.ui.fragments.navigation.home

import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.showToastShort
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentHomeBinding
import com.huyhieu.mydocuments.models.HomeMenu
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.MenuAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val adapterMenu by lazy { MenuAdapter() }
    private var lstMenus = mutableListOf<MenuForm>()
//        MenuForm(name = "Network API") {
//            navigate(MyNavHost.Main, MainDirections.toRecallAPI)
//        },
//        MenuForm(name = "Widget") {x
//            navigate(MyNavHost.Main, MainDirections.toCountdown)
//        },
//        MenuForm(name = "Chart") {
//            navigate(MyNavHost.Main, MainDirections.toCanvasChart)
//        },
//        MenuForm(name = "Notification") {
//            navigate(MyNavHost.Main, MainDirections.toMultipleAPI)
//        },
//        MenuForm(name = "Components") {
//            startActivity(Intent(mActivity, BluetoothActivity::class.java))
//        },
//        MenuForm(name = "Systems") {
//            startActivity(Intent(mActivity, FFmpegKitActivity::class.java))
//        },
//        MenuForm(name = "Dialog") {
//            mActivity.supportFragmentManager?.let {
//                MyDialog.getInstance().show(it, null)
//            }
//        },
//        MenuForm(name = "Language") {
//            navigate(MyNavHost.Main, MainDirections.toSteps)
//        },
//        MenuForm(name = "Theme") {
//            navigate(MyNavHost.Main, MainDirections.toSteps)
//        }
//        //Fake
//    )

    override fun FragmentHomeBinding.onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar()
        //showNavigationBottom()
        //setTabNavigationBottom(com.huyhieu.library.components.UTab.TAB_HOME)
        homeVM.loadMenuFromAsset()
        homeVM.fetchGitHubCommitted()
        setupMenu()
    }

    override fun FragmentHomeBinding.onMyLiveData(savedInstanceState: Bundle?) {
        homeVM.menuLiveData.observe {
            it ?: return@observe
            lstMenus = it
            adapterMenu.fillData(lstMenus)
        }
        homeVM.githubCommittedLiveData.observe {
            it ?: return@observe
            showToastShort("GitHub: ${it.first().commit.committer.name} - ${it.first().commit.message}")
        }
    }

    private fun FragmentHomeBinding.setupMenu() {
        rcvMenu.apply {
            setHasFixedSize(true)
            adapter = adapterMenu
        }
        adapterMenu.itemClick = {
            when (it.type) {
                HomeMenu.MENU_THEME -> {
                    showToastShort("${it.name} coming soon!")
                }
                HomeMenu.MENU_LANGUAGE -> {
                    showToastShort("${it.name} coming soon!")
                }
                HomeMenu.MENU_NETWORK_API -> {
                    showToastShort("${it.name} coming soon!")
                }
                HomeMenu.MENU_WIDGET -> {
                    showToastShort("${it.name} coming soon!")
                }
                HomeMenu.MENU_CHART -> {
                    showToastShort("${it.name} coming soon!")
                }
                HomeMenu.MENU_NOTIFICATION -> {
                    showToastShort("${it.name} coming soon!")
                }
                HomeMenu.MENU_SYSTEM -> {
                    showToastShort("${it.name} coming soon!")
                }
                HomeMenu.MENU_DIALOG -> {
                    showToastShort("${it.name} coming soon!")
                }
                HomeMenu.MENU_OTHERS -> {
                    showToastShort("${it.name} coming soon!")
                }
            }
        }
    }

}