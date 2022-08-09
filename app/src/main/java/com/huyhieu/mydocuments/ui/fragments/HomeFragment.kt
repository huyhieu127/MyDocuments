package com.huyhieu.mydocuments.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentHomeBinding
import com.huyhieu.mydocuments.others.enums.MenuType
import com.huyhieu.mydocuments.ui.activities.bluetooth.BluetoothActivity
import com.huyhieu.mydocuments.ui.activities.ffmmpegkit.FFmpegKitActivity
import com.huyhieu.mydocuments.ui.activities.main.HomeViewModel
import com.huyhieu.mydocuments.ui.activities.main.MyDialog
import com.huyhieu.mydocuments.ui.activities.multipleapi.MultipleAPIActivity
import com.huyhieu.mydocuments.ui.activities.notification.NotificationActivity
import com.huyhieu.mydocuments.ui.activities.steps.StepsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
        initListMenu()
    }

    override fun addEvents(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
    }

    private fun initListMenu() {
        mBinding.rcvMenu.apply {
            setHasFixedSize(true)
            adapter = viewModel.adapterMenu
        }
        viewModel.adapterMenu.apply {
            fillData(viewModel.lstMenus)
            itemClick = { menuForm ->
                when (menuForm.type) {
                    MenuType.FFmpegKit -> {
                        startActivity(Intent(mActivity, FFmpegKitActivity::class.java))
                    }
                    MenuType.MultipleAPI -> {
                        startActivity(Intent(mActivity, MultipleAPIActivity::class.java))
                    }
                    MenuType.Bluetooth -> {
                        startActivity(Intent(mActivity, BluetoothActivity::class.java))
                    }
                    MenuType.Notification -> {
                        startActivity(Intent(mActivity, NotificationActivity::class.java))
                    }
                    MenuType.Steps -> {
                        startActivity(Intent(mActivity, StepsActivity::class.java))
                    }
                    MenuType.More -> {
                        mActivity?.supportFragmentManager?.let {
                            MyDialog.getInstance().show(it, null)
                        }
                    }
                }
            }
        }
    }

}