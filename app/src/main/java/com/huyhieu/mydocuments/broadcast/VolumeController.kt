package com.huyhieu.mydocuments.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity

class VolumeController : BroadcastReceiver() {

    private var context: Context? = null
    var onAdjustVolume: ((volume: Int) -> Unit)? = null

    private var volume = 0
    private var audioManager: AudioManager? = null


    private var isRunning = false

    override fun onReceive(context: Context, intent: Intent?) {
        intent ?: return
        val newVolume = intent.extras?.getInt("android.media.EXTRA_VOLUME_STREAM_VALUE") ?: 0
        if (volume != newVolume) {
            volume = newVolume
            onAdjustVolume?.invoke(volume)
        }
    }

    fun create(context: Context?) {
        context ?: return
        this.context = context
        audioManager =
            context.applicationContext?.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager?
    }

    fun register() {
        context ?: return
        val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        context?.registerReceiver(this, filter)
    }

    fun unregister() {
        context ?: return
        context?.unregisterReceiver(this)
        isRunning = false
    }

    fun currentVolume() = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun maxVolume() = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    fun setVolume(volume: Int, flags: Int = AudioManager.FLAG_SHOW_UI) {
        audioManager?.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            volume,
            flags //0: Hide UI
        )
    }
}