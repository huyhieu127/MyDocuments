package com.huyhieu.mydocuments.ui.activities.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.huyhieu.library.extensions.showToastShort
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityBluetoothBinding

class BluetoothActivity : BaseActivity<ActivityBluetoothBinding>() {


    @RequiresApi(Build.VERSION_CODES.M)
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            scanNow()
        }

    override fun binding() = ActivityBluetoothBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
        registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {
        when (p0?.id) {
            vb.tvMessage.id -> {
                scanNow()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun scanNow() {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            resultLauncher.launch(enableBtIntent)

        } else {
            checkPermission {
                val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                pairedDevices?.forEach { device ->
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    vb.tvMessage.text =
                        ("Results<pairedDevices>: $deviceName - $deviceHardwareAddress")
                }

                bluetoothAdapter?.startDiscovery()
            }
        }
    }

    private fun checkPermission(function: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showToastShort("Permission denied!")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    "android.permission.BLUETOOTH_CONNECT",
                    "android.permission.BLUETOOTH_SCAN",
                ),
                0
            )
            return
        } else {
            function.invoke()
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.run {
                        checkPermission {
                            val deviceName = device.name
                            val deviceHardwareAddress = device.address // MAC address
                            vb.tvMessage.text =
                                ("Results<device>: $deviceName - $deviceHardwareAddress")
                        }
                    }
                }
                else -> vb.tvMessage.text = ("Not found!")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}