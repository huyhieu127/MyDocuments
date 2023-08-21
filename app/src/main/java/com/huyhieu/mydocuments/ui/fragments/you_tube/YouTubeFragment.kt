package com.huyhieu.mydocuments.ui.fragments.you_tube

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.huyhieu.library.extensions.addSpannable
import com.huyhieu.library.extensions.setNavigationBarColor
import com.huyhieu.library.extensions.setSpannable
import com.huyhieu.library.extensions.showToastShort
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentYouTubeBinding
import com.huyhieu.mydocuments.models.YouTubeForm
import com.huyhieu.mydocuments.utils.init


class YouTubeFragment : BaseFragment<FragmentYouTubeBinding>() {
    private val player by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val adapterLocal = YouTubeAdapter()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d(">>>>>>>>>>>>>>>", "Selected URI: $uri")
                //val mediaItems = uri.map { MediaItem.fromUri(it) }
                //player.addMediaItems(mediaItems)
                playVideo(uri.toString())
            }
        }

    private val lstLocal = mutableListOf(
        YouTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 1",
            authorName = "Đen Vâu Official"
        ),
        YouTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 2",
            authorName = "Đen Vâu Official"
        ),
        YouTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 3",
            authorName = "Đen Vâu Official"
        ),
        YouTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 4",
            authorName = "Đen Vâu Official"
        ),
        YouTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 5",
            authorName = "Đen Vâu Official"
        ),
        YouTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 6",
            authorName = "Đen Vâu Official"
        ),
        YouTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 7",
            authorName = "Đen Vâu Official"
        ),
        YouTubeForm(
            title = "Đen x JustaTee - Đi Về Nhà (M/V) 8",
            authorName = "Đen Vâu Official"
        ),
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        mActivity.setNavigationBarColor(R.color.colorBlackYoutube)
        initView()
        setClickViews(playerView, imgPlayWindow, imgCloseWindow)

        vb.playerView.player = player
        vb.playerView.useController = false
        //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
    }

    private fun playVideo(uri: String) {
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        player.play()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() = with(vb) {
        rcvHome.init(adapterLocal)
        frameWatching.rcvWatching.init(adapterLocal)

        adapterLocal.fillData(lstLocal)
        val content = "21 N lượt xem  1 tháng trước  #makingmyway #nonstop2023 #nhactreremix"
        frameWatching.tvHashTag.setSpannable(
            content = content,
            subText = "#makingmyway",
            colorId = R.color.blue,
            actionClick = {
                showToastShort(it)
            }
        ).addSpannable(
            subText = "#nonstop2023",
            colorId = R.color.blue,
            actionClick = {
                showToastShort(it)
            }
        ).addSpannable(
            subText = "#nhactreremix",
            colorId = R.color.blue,
            actionClick = {
                showToastShort(it)
            }
        )
    }

    override fun FragmentYouTubeBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            playerView -> {
                logDebug("Start")
                root.transitionToStart()
            }

            imgPlayWindow -> {
                logDebug("Play")
            }

            imgCloseWindow -> {
                logDebug("Close")
            }
        }
    }

}