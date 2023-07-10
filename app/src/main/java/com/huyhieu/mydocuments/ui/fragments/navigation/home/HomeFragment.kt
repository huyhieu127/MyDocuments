package com.huyhieu.mydocuments.ui.fragments.navigation.home

import android.os.Bundle
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.showToastShort
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentHomeBinding
import com.huyhieu.mydocuments.models.HomeMenu
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.ui.fragments.dialog.toast.showToastSuccess
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.MenuAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val adapterMenu by lazy { MenuAdapter() }
    private var lstMenus = mutableListOf<MenuForm>()

    override fun FragmentHomeBinding.onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar()
        //showNavigationBottom()
        //setTabNavigationBottom(com.huyhieu.library.components.UTab.TAB_HOME)
        homeVM.loadMenuFromAsset()
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
        }
    }

    private fun FragmentHomeBinding.setupMenu() {
        rcvMenu.apply {
            setHasFixedSize(true)
            adapter = adapterMenu
        }
        adapterMenu.itemClick = {
            when (it.type) {
                HomeMenu.MENU_ABOUT -> {
                    PopupNotifyFragment.newInstance().showBottomSheet(parentFragmentManager)
                }
                HomeMenu.MENU_THEME -> {
                    showToastSuccess()
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