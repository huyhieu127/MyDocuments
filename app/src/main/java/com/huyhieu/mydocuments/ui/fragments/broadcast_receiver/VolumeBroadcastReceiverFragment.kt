package com.huyhieu.mydocuments.ui.fragments.broadcast_receiver

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.broadcast.VolumeController
import com.huyhieu.mydocuments.databinding.FragmentVolumeBroadcastReceiverBinding

class VolumeBroadcastReceiverFragment : BaseFragment<FragmentVolumeBroadcastReceiverBinding>() {
    private var volumeController: VolumeController? = null
    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        volumeController = VolumeController().apply {
            create(requireContext())
            onAdjustVolume = {
                vb.tvResult.text = "Volume: $it/${volumeController?.maxVolume()}"
            }
        }
        volumeController?.let {
            vb.tvResult.text = "Volume: ${it.currentVolume()}/${it.maxVolume()}"
        }

        setClickViews(vb.btnVolumeDown, vb.btnVolumeUp)
    }

    override fun FragmentVolumeBroadcastReceiverBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            vb.btnVolumeDown -> {
                val currentVolume = volumeController?.currentVolume() ?: 0
                val value = if (currentVolume - 1 < 0) 0 else currentVolume - 1
                volumeController?.setVolume(value, 0)
            }

            vb.btnVolumeUp -> {
                val currentVolume = volumeController?.currentVolume() ?: 0
                val value = if (currentVolume + 1 > (volumeController?.maxVolume()
                        ?: 0)
                ) volumeController?.maxVolume() ?: 0 else currentVolume + 1
                volumeController?.setVolume(value, 0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        volumeController?.register()
    }

    override fun onPause() {
        super.onPause()
        volumeController?.unregister()
    }
}