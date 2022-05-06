package com.huyhieu.mydocuments.ui.activities.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityMainBinding
import com.huyhieu.mydocuments.others.enums.MenuType
import com.huyhieu.mydocuments.ui.activities.bluetooth.BluetoothActivity
import com.huyhieu.mydocuments.ui.activities.multipleapi.MultipleAPIActivity
import com.huyhieu.mydocuments.ui.activities.notification.NotificationActivity
import com.huyhieu.mydocuments.utils.extensions.showToastShort
import com.huyhieu.mydocuments.utils.logDebug
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    @Inject
    lateinit var viewModel: MainViewModel

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        initListMenu()
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
                    MenuType.MultipleAPI -> {
                        startActivity(Intent(this@MainActivity, MultipleAPIActivity::class.java))
                    }
                    MenuType.Bluetooth -> {
                        startActivity(Intent(this@MainActivity, BluetoothActivity::class.java))
                    }
                    MenuType.Notification -> {
                        startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
                    }
                    MenuType.More -> {
                        MyDialog.getInstance().show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
        logDebug("Test push Event Github action")
    }

    override fun onClick(p0: View?) {
    }
}