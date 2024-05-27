package com.huyhieu.mydocuments.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

enum class PowerConnectionStatus {
    USB_CHARGING, AC_CHARGING, NOT_CHARGING
}

class PowerConnectionReceiver : BroadcastReceiver() {
    private var context: Context? = null
    var powerConnectionStatus: ((PowerConnectionStatus) -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        val action = intent.action
        if (action != null && action == Intent.ACTION_BATTERY_CHANGED) {
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging =
                status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

            val chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
            val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

            if (isCharging) {
                if (usbCharge) {
                    powerConnectionStatus?.invoke(PowerConnectionStatus.USB_CHARGING)
                } else if (acCharge) {
                    powerConnectionStatus?.invoke(PowerConnectionStatus.AC_CHARGING)
                }
            } else {
                powerConnectionStatus?.invoke(PowerConnectionStatus.NOT_CHARGING)
            }
        }
    }

    fun register(context: Context?) {
        context ?: return
        this.context = context
        val intentFilter = IntentFilter().also {
            it.addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        context.registerReceiver(this@PowerConnectionReceiver, intentFilter)
    }

    fun unregister() {
        context ?: return
        context?.unregisterReceiver(this)
    }
}