package com.huyhieu.mydocuments.ui.fragments.broadcast_receiver

import android.os.Bundle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.broadcast.PowerConnectionReceiver
import com.huyhieu.mydocuments.databinding.FragmentPowerConnectionReceiverBinding


class PowerConnectionReceiverFragment : BaseFragment<FragmentPowerConnectionReceiverBinding>() {
    private var powerConnectionReceiver: PowerConnectionReceiver? = null
    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        powerConnectionReceiver = PowerConnectionReceiver()
        powerConnectionReceiver?.powerConnectionStatus = {
            val status = it.name
            vb.tvResult.text = "Power Connection Status $status"
        }
    }

    override fun onResume() {
        super.onResume()
        powerConnectionReceiver?.register(requireContext())
    }

    override fun onPause() {
        super.onPause()
        powerConnectionReceiver?.unregister()
    }
}