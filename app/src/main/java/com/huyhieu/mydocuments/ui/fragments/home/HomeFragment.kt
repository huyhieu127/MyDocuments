package com.huyhieu.mydocuments.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentHomeBinding
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.directions.MainDirections
import com.huyhieu.mydocuments.navigation.navigate
import com.huyhieu.mydocuments.ui.activities.bluetooth.BluetoothActivity
import com.huyhieu.mydocuments.ui.activities.ffmmpegkit.FFmpegKitActivity
import com.huyhieu.mydocuments.ui.fragments.home.components.HomeVM
import com.huyhieu.mydocuments.ui.fragments.home.components.MenuAdapter
import com.huyhieu.mydocuments.ui.fragments.home.components.MyDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModel: HomeVM
    private val adapterMenu by lazy { MenuAdapter() }
    private var lstMenus = mutableListOf(
        MenuForm(name = "Motion Card") {
            navigate(MyNavHost.Main, MainDirections.toMotionCard)
        },
        MenuForm(name = "Canvas chart") {
            navigate(MyNavHost.Main, MainDirections.toCanvasChart)
        },
        MenuForm(name = "Mobile FFmpeg Kit") {
            startActivity(Intent(mActivity, FFmpegKitActivity::class.java))
        },
        MenuForm(name = "Call multiple API") {
            navigate(MyNavHost.Main, MainDirections.toMultipleAPI)
        },
        MenuForm(name = "Bluetooth") {
            startActivity(Intent(mActivity, BluetoothActivity::class.java))
        },
        MenuForm(name = "Steps") {
            navigate(MyNavHost.Main, MainDirections.toSteps)
        },
        MenuForm(name = "More") {
            mActivity.supportFragmentManager?.let {
                MyDialog.getInstance().show(it, null)
            }
        }
        //Fake
    )

    override fun FragmentHomeBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar()
        //showNavigationBottom()
        //setTabNavigationBottom(com.huyhieu.library.components.UTab.TAB_HOME)
        initListMenu()
    }

    private fun FragmentHomeBinding.initListMenu() {
        rcvMenu.apply {
            setHasFixedSize(true)
            adapter = adapterMenu
        }
        adapterMenu.fillData(lstMenus)
    }

}