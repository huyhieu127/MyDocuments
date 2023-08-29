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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TikTokFragment : BaseFragmentVM<FragmentTikTokBinding, TikTokVM>() {

    private val forYouAdapter by lazy { TikTokPlayerAdapter() }

    private val player by lazy {
        ExoPlayer.Builder(requireContext()).build().apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    private val vpCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            val y = (-positionOffsetPixels).toFloat()
//            vb.playerView.y = y
//            vb.lnContainer.y = y
            val x = (-positionOffsetPixels).toFloat()
            //logDebug("$positionOffsetPixels  --------- $position -> $positionOffset")
            vb.playerView.x = x
            vb.frameController.root.x = x
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            player.pause()
            jobTime?.cancel()

            val oldIndex = player.currentMediaItemIndex
            val seekTime = forYouAdapter.listData[position].timePlayed

            //Save time played of video played
            val timePlayed = player.currentPosition
            forYouAdapter.listData[oldIndex].timePlayed = timePlayed

            //Save last frame of video played
            screenshotLatestFrame(oldIndex)

            //Play video in this page
            vb.playerView.isInvisible = true
            player.seekTo(position, seekTime)
            val duration = player.duration
            val time = (seekTime * 100) / duration
            vb.frameController.seekBarDuration.progress = time.toInt()
            player.play()
        }
    }

    private val playerCallback = object : Player.Listener {
        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            val isShow = isLoading && !player.isPlaying
            vb.prgLoading.isVisible = isShow
            logDebug("onIsLoadingChanged $isShow")
        }

        //End video
        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            if (reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION) {
                lifecycleScope.launch {
                    jobTime?.cancel()
                    vm.setSeekToProgressBar(100)
                    delay(1000)
                    if (player.isPlaying) getCurrentPlayerPosition()
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (isPlaying) {
                vb.playerView.isInvisible = false
                getCurrentPlayerPosition()
            }
            if (isPlaying) {
                vb.prgLoading.isVisible = false
            } else {
                vb.prgLoading.isVisible = player.isLoading
            }

            logDebug("$isPlaying -- ${player.isLoading}")
        }
    }

    @OptIn(UnstableApi::class)
    private fun screenshotLatestFrame(index: Int) {
//        tryCatch {
//            val bitmap = (vb.playerView.videoSurfaceView as TextureView).bitmap
//            forYouAdapter.listData[index].thumbnail = bitmap
//            logDebug("screenshotLatestFrame - $index - ${bitmap != null}")
//            if (forYouAdapter.listData[index].thumbnail != null) {
//                forYouAdapter.notifyItemChanged(index)
//            }
//        }
    }

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setNavigationBarColor(R.color.black)
        initPlayerView()
        initViewForYou()
        vb.apply {
            setClickViews(frameController.imgWhiteHeart, frameController.imgLottieHeart)
        }
    }

    override fun FragmentTikTokBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            frameController.imgWhiteHeart -> {
                with(frameController.imgLottieHeart) {
                    isVisible = true
                    scaleX = 1F
                    scaleY = 1F
                    playAnimation()
                }
            }

            frameController.imgLottieHeart -> {
                with(frameController.imgLottieHeart) {
                    animate().scaleX(0F).scaleY(0F).withEndAction {
                        isInvisible = true
                    }
                }
            }
        }
    }

    override fun onMyLiveData(savedInstanceState: Bundle?) {
        vm.currentPosition.observeNoneNull {
            vb.frameController.seekBarDuration.setProgress(it.toInt(), true)
        }
    }

    @OptIn(UnstableApi::class)
    private fun initPlayerView() = with(vb) {
        playerView.player = player
        playerView.useController = false
        player.addListener(playerCallback)

        vb.frameController.seekBarDuration.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
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
                getCurrentPlayerPosition()
            }

        })
    }

    private fun initViewForYou() = with(vb) {
        vp2ForYou.adapter = forYouAdapter
        loadForYou()
    }

    private fun loadForYou() {
        val json = readAssets("explore.json")
        val jsonObject = json.toData<JsonObject>()
        val jsonVideos = jsonObject?.get("videos")?.asJsonArray

        val listVideo = jsonVideos.toJson().toData<List<String>>() ?: mutableListOf()
        val listTikTokVideos = listVideo.map { TikTokVideoForm(url = it) }.toMutableList()
        listTikTokVideos.first().isPlaying = true
        forYouAdapter.fillData(listTikTokVideos)
        vb.vp2ForYou.registerOnPageChangeCallback(vpCallback)
        vb.vp2ForYou.setOnTouchListener(View.OnTouchListener { v, motionEvent ->
            return@OnTouchListener false
        })

        playVideo(listTikTokVideos)
    }

    private fun playVideo(videos: MutableList<TikTokVideoForm>) {
        val mediaItems = videos.map { MediaItem.fromUri(it.url) }
        player.addMediaItems(mediaItems)
        player.prepare()
    }

    private var jobTime: Job? = null
    private fun getCurrentPlayerPosition() {
        jobTime?.cancel()
        jobTime = lifecycleScope.launch {
            val duration = player.duration
            val position = player.currentPosition
            val time = (position * 100) / duration
            vm.setSeekToProgressBar(time)
            delay(1000)
            if (player.isPlaying) getCurrentPlayerPosition()
        }
    }

    override fun onDestroyView() {
        vb.vp2ForYou.unregisterOnPageChangeCallback(vpCallback)
        player.removeListener(playerCallback)
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        logDebug("onDestroy")
    }
}