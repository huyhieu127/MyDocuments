package com.huyhieu.mydocuments.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentHomeBinding
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.directions.MainDirections
import com.huyhieu.mydocuments.navigation.navigate
import com.huyhieu.mydocuments.others.enums.MenuType
import com.huyhieu.mydocuments.ui.activities.bluetooth.BluetoothActivity
import com.huyhieu.mydocuments.ui.activities.ffmmpegkit.FFmpegKitActivity
import com.huyhieu.mydocuments.ui.fragments.home.components.HomeVM
import com.huyhieu.mydocuments.ui.fragments.home.components.MyDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModel: HomeVM

    override fun FragmentHomeBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar()
        //showNavigationBottom()
        //setTabNavigationBottom(com.huyhieu.library.components.UTab.TAB_HOME)
        initListMenu()
    }

    private fun FragmentHomeBinding.initListMenu() {
        rcvMenu.apply {
            setHasFixedSize(true)
            adapter = viewModel.adapterMenu
        }
        viewModel.adapterMenu.apply {
            fillData(viewModel.lstMenus)
            itemClick = { menuForm ->
                when (menuForm.type) {
                    MenuType.GUIDE -> {
                        navigate(MyNavHost.Main, MainDirections.toGuide)
                    }
                    MenuType.MOTION -> {
                        navigate(MyNavHost.Main, MainDirections.toMotionCard)
                    }
                    MenuType.FFMPEG_KIT -> {
                        startActivity(Intent(mActivity, FFmpegKitActivity::class.java))
                    }
                    MenuType.MULTIPLE_API -> {
                        navigate(MyNavHost.Main, MainDirections.toMultipleAPI)
                    }
                    MenuType.BLUETOOTH -> {
                        startActivity(Intent(mActivity, BluetoothActivity::class.java))
                    }
                    MenuType.STEPS -> {
                        navigate(MyNavHost.Main, MainDirections.toSteps)
                    }
                    MenuType.COMPONENTS -> {
                        mActivity.supportFragmentManager?.let {
                            MyDialog.getInstance().show(it, null)
                        }
                    }
                }
            }
        }
    }

}