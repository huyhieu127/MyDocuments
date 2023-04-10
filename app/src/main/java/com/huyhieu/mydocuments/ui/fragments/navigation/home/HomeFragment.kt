package com.huyhieu.mydocuments.ui.fragments.navigation.home

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
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.HomeVM
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.MenuAdapter
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.MyDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModel: HomeVM
    private val adapterMenu by lazy { MenuAdapter() }
    private var lstMenus = mutableListOf(
        MenuForm(name = "Remote API") {
            navigate(MyNavHost.Main, MainDirections.toRecallAPI)
        },
        MenuForm(name = "Widgets") {
            navigate(MyNavHost.Main, MainDirections.toCountdown)
        },
        MenuForm(name = "Charts") {
            navigate(MyNavHost.Main, MainDirections.toCanvasChart)
        },
        MenuForm(name = "Notifications") {
            navigate(MyNavHost.Main, MainDirections.toMultipleAPI)
        },
        MenuForm(name = "Components") {
            startActivity(Intent(mActivity, BluetoothActivity::class.java))
        },
        MenuForm(name = "Systems") {
            startActivity(Intent(mActivity, FFmpegKitActivity::class.java))
        },
        MenuForm(name = "Dialogs") {
            mActivity.supportFragmentManager?.let {
                MyDialog.getInstance().show(it, null)
            }
        },
        MenuForm(name = "Steps") {
            navigate(MyNavHost.Main, MainDirections.toSteps)
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