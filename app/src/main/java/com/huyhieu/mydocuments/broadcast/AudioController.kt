package com.huyhieu.mydocuments.broadcast

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity

class AudioController(
    val activity: Activity?,
    val onAdjustAudio: ((volume: Int) -> Unit)
) : BroadcastReceiver() {

    private var volume = 0
    val audioManager: AudioManager? by lazy {
        activity?.applicationContext?.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager?
    }

    override fun onReceive(context: Context, intent: Intent) {
        val newVolume = intent.extras?.get("android.media.EXTRA_VOLUME_STREAM_VALUE") as Int
        if (volume != newVolume) {
            volume = newVolume
            onAdjustAudio.invoke(volume)
        }
    }

    fun register() {
        activity ?: return
        val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        activity.registerReceiver(this, filter)
    }

    fun unregister() {
        activity ?: return
        activity.unregisterReceiver(this)
    }

    fun currentVolume() = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun maxVolume() = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    fun setVolume(volume: Int) {
        audioManager?.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            volume,
            AudioManager.FLAG_SHOW_UI //0: Hide UI
        )
    }
}