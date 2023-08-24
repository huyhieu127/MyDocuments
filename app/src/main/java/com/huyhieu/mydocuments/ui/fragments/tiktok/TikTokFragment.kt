package com.huyhieu.mydocuments.ui.fragments.tiktok

import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.JsonObject
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentTikTokBinding
import com.huyhieu.mydocuments.libraries.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.models.TikTokVideoForm
import com.huyhieu.mydocuments.utils.readAssets
import com.huyhieu.mydocuments.utils.toData
import com.huyhieu.mydocuments.utils.toJson


class TikTokFragment : BaseFragment<FragmentTikTokBinding>() {
    private val forYouAdapter by lazy { TikTokPlayerAdapter() }
    private val player by lazy {
        ExoPlayer.Builder(requireContext()).build().apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    private val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            //logDebug("super.onPageScrolled($position, $positionOffset, $positionOffsetPixels) ${vb.vp2ForYou.currentItem}")
            vb.playerView.y = (-positionOffsetPixels).toFloat()
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val currentMediaItemIndex = player.currentMediaItemIndex
            val seekTime = forYouAdapter.listData[position].timePlayed
            //Change state
            //forYouAdapter.update(position, currentMediaItemIndex)

            //Save time played of video played
            val timePlayed = player.currentPosition
            forYouAdapter.listData[currentMediaItemIndex].timePlayed = timePlayed

            //Play video in this page
            if (position > currentMediaItemIndex) {
                vb.playerView.isInvisible = true
                player.seekToNextMediaItem()
                //player.seekTo(seekTime)
            }
            if (position < currentMediaItemIndex) {
                vb.playerView.isInvisible = true
                player.seekToPreviousMediaItem()
                //player.seekTo(seekTime)
            }

        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            //logDebug("super.onPageScrollStateChanged($state)")
        }
    }

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setNavigationBarColor(R.color.black)
        initPlayerView()
        initViewForYou()
    }

    private fun initPlayerView() = with(vb) {
        playerView.player = player
        playerView.useController = false

        player.addListener(object : Player.Listener {
            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                if (!isLoading) {
                    playerView.isInvisible = false
                }
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
        vb.vp2ForYou.registerOnPageChangeCallback(callback)

        playVideo(listTikTokVideos)
    }

    private fun playVideo(videos: MutableList<TikTokVideoForm>) {
        val mediaItems = videos.map { MediaItem.fromUri(it.url) }
        player.addMediaItems(mediaItems)
        player.prepare()
    }

    private fun setSizePlayer() = with(vb) {
        vp2ForYou.post {
            playerView.layoutParams = playerView.layoutParams.apply {
                width = vp2ForYou.width
                height = vp2ForYou.height
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vb.vp2ForYou.unregisterOnPageChangeCallback(callback)
    }
}