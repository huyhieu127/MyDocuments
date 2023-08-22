package com.huyhieu.mydocuments.ui.fragments.you_tube

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.huyhieu.library.extensions.addSpannable
import com.huyhieu.library.extensions.onTransitionCompleted
import com.huyhieu.library.extensions.setNavigationBarColor
import com.huyhieu.library.extensions.setSpannable
import com.huyhieu.library.extensions.setTransitionTo
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
        setClickViews(
            playerView,
            framePlayerCollapse.imgPlayWindow,
            framePlayerCollapse.imgCloseWindow
        )

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

        adapterLocal.onClickItem = {
            showTransitionWatching()
        }
        root.onTransitionCompleted { _, currentId ->
            checkEnableTransition(currentId)
        }
    }

    override fun FragmentYouTubeBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            playerView -> {
                logDebug("Start")
                root.transitionToStart()
            }

            framePlayerCollapse.imgPlayWindow -> {
                logDebug("Play")
            }

            framePlayerCollapse.imgCloseWindow -> {
                hideTransitionWatching()
            }
        }
    }

    private fun showTransitionWatching() = with(vb) {
        if (root.currentState == R.id.miniWindow) {
            root.transitionToStart()
        }else{
            root.setTransition(R.id.transitionWatching)
            root.transitionToEnd()
        }
    }

    private fun hideTransitionWatching() = with(vb) {
        root.setTransitionTo(R.id.miniWindow, R.id.normal)
        root.transitionToStart()
    }

    private fun checkEnableTransition(id: Int) {
        enableTransitionWatching(id)
        enableTransitionCollapse(id)
    }

    private fun enableTransitionWatching(id: Int) = with(vb) {
//        logDebug("enableTransitionWatching: ${id == R.id.transitionCollapse}")
//        if (id == R.id.transitionCollapse) {
//            root.setTransition(R.id.transitionWatching)
//        }
//        root.enableViewTransition(R.id.transitionWatching, id == R.id.transitionCollapse)
//        root.enableTransition(R.id.transitionWatching, id == R.id.transitionCollapse)
    }

    private fun enableTransitionCollapse(id: Int) = with(vb) {
        logDebug("enableTransitionCollapse: ${id == R.id.fullWindow}")
        if (id == R.id.fullWindow) {
            root.setTransition(R.id.transitionCollapse)
        }
        root.enableViewTransition(R.id.transitionCollapse, id != R.id.normal)
        root.enableTransition(R.id.transitionCollapse, id != R.id.normal)
    }
}