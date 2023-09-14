package com.huyhieu.mydocuments.ui.fragments.tiktok

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.JsonObject
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragmentVM
import com.huyhieu.mydocuments.databinding.FragmentTikTokBinding
import com.huyhieu.mydocuments.libraries.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.models.TikTokVideoForm
import com.huyhieu.mydocuments.utils.readAssets
import com.huyhieu.mydocuments.utils.toData
import com.huyhieu.mydocuments.utils.toJson
import com.huyhieu.mydocuments.utils.tryCatch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@AndroidEntryPoint
class TikTokFragment : BaseFragmentVM<FragmentTikTokBinding, TikTokVM>() {

    private val forYouAdapter by lazy { TikTokPlayerAdapter(requireContext()) }

    private val vpCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            jobTime?.cancel()

            val player = forYouAdapter.storedPlayers[position]
            val oldIndex = player.currentMediaItemIndex
            val seekTime = forYouAdapter.listVideo[position].timePlayed

            //Save time played of video played
            val timePlayed = player.currentPosition
            forYouAdapter.listVideo[oldIndex].timePlayed = timePlayed

            //Play video in this page
            player.seekTo(position, seekTime)
            val duration = player.duration
            val time = (seekTime * 100) / duration
            tryCatch {
                setupSeekBar(player)
                vb.seekBarDuration.progress = time.toInt()
            }
            player.play()
        }
    }

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setNavigationBarColor(R.color.black)
        initViewForYou()
    }

    override fun onMyLiveData(savedInstanceState: Bundle?) {
        vm.currentPosition.observeNoneNull {
            vb.seekBarDuration.setProgress(it.toInt(), true)
        }
    }

    private fun setupSeekBar(player: Player) {
        getCurrentPlayerPosition(player)
        vb.seekBarDuration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(v: SeekBar?, progress: Int, isFromUser: Boolean) {
                if (isFromUser) {
                    val duration = player.duration
                    val time = ((progress / 100F) * duration).toLong()
                    player.seekTo(time)
                }
            }

            override fun onStartTrackingTouch(v: SeekBar?) {
                jobTime?.cancel()
                player.pause()
            }

            override fun onStopTrackingTouch(v: SeekBar?) {
                player.play()
                getCurrentPlayerPosition(player)
            }
        })
    }

    private fun initViewForYou() = with(vb) {
        vp2ForYou.adapter = forYouAdapter
        loadForYou()
    }

    @OptIn(UnstableApi::class)
    private fun loadForYou() {
        val json = readAssets("explore.json")
        val jsonObject = json.toData<JsonObject>()
        val jsonVideos = jsonObject?.get("videos")?.asJsonArray

        val listVideo = jsonVideos.toJson().toData<List<String>>() ?: mutableListOf()
        val listTikTokVideos = listVideo.map { TikTokVideoForm(url = it) }.toMutableList()
        listTikTokVideos.first().isPlaying = true
        forYouAdapter.fillData(listTikTokVideos)
        vb.vp2ForYou.registerOnPageChangeCallback(vpCallback)
    }

    private var jobTime: Job? = null
    private fun getCurrentPlayerPosition(player: Player) {
        jobTime?.cancel()
        jobTime = lifecycleScope.launch {
            val duration = player.duration
            val position = player.currentPosition
            val time = (position * 100) / duration
            vm.setSeekToProgressBar(time)
            delay(1000)
            if (player.isPlaying) getCurrentPlayerPosition(player)
        }
    }

    override fun onResume() {
        super.onResume()
        tryCatch {
            val currentIndex = vb.vp2ForYou.currentItem
            getCurrentPlayerPosition(forYouAdapter.storedPlayers[currentIndex])
            forYouAdapter.playVideo(currentIndex)
        }
    }

    override fun onPause() {
        super.onPause()
        tryCatch {
            jobTime?.cancel()
            forYouAdapter.pauseVideo(vb.vp2ForYou.currentItem)
        }
    }

    override fun onDestroyView() {
        vb.vp2ForYou.unregisterOnPageChangeCallback(vpCallback)
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        forYouAdapter.releaseAllPlayer()
        logDebug("onDestroy")
    }
}