package com.huyhieu.mydocuments.ui.fragments.you_tube

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentYouTubeBinding
import com.huyhieu.mydocuments.libraries.extensions.addSpannable
import com.huyhieu.mydocuments.libraries.extensions.onTransitionCompleted
import com.huyhieu.mydocuments.libraries.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.libraries.extensions.setSpannable
import com.huyhieu.mydocuments.libraries.extensions.setTransitionTo
import com.huyhieu.mydocuments.libraries.extensions.showToastShort
import com.huyhieu.mydocuments.models.YouTubeForm
import com.huyhieu.mydocuments.utils.init
import com.huyhieu.mydocuments.utils.loadMedia
import com.huyhieu.mydocuments.utils.tryCatch
import okhttp3.internal.toImmutableList


class YouTubeFragment : BaseFragment<FragmentYouTubeBinding>() {
    private val player by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val adapterVideos = YouTubeAdapter()
    private val adapterWatching = YouTubeAdapter()
    private var videosLocal = mutableListOf<YouTubeForm>()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                loadVideoMedia()
            } else {
                showToastShort("Permission denied!")
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        mActivity.setNavigationBarColor(R.color.colorBlackYoutube)
        initView()
        setClickViews(
            playerView,
            framePlayerCollapse.imgCollapsePlay,
            framePlayerCollapse.imgCollapseClose
        )

        vb.playerView.player = player
        vb.playerView.useController = false
        player.hasNextMediaItem()
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_READY) {
                    logDebug("Player.STATE_READY ${player.currentMediaItemIndex}")
                    tryCatch {
                        val videoCurrent = adapterWatching.listData[player.currentMediaItemIndex]
                        fillData(videoCurrent)
                    }
                }
            }
        })
        checkPermissionMedia()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() = with(vb) {
        rcvHome.init(adapterVideos)
        adapterVideos.onClickItemWithPosition = { pos, item ->
            logDebug("${adapterVideos.listData.size} -- $pos")
            clickVideo(adapterVideos.listData, pos, item)
        }

        frameWatching.rcvWatching.init(adapterWatching)
        adapterWatching.onClickItemWithPosition = { pos, item ->
            clickVideo(adapterWatching.listData, pos, item)
        }
        root.onTransitionCompleted { _, currentId ->
            checkEnableTransition(currentId)
        }
    }

    private fun clickVideo(listOld: MutableList<YouTubeForm>, pos: Int, item: YouTubeForm) {
        var playList = mutableListOf<YouTubeForm>()
        tryCatch({
            playList = listOld.subList(pos, listOld.size).toMutableList()
        }) {
            playList = mutableListOf()
        }
        fillData(item)

        prepareVideos(playList)
        showTransitionWatching()
    }

    override fun FragmentYouTubeBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            playerView -> {
                root.transitionToStart()
            }

            framePlayerCollapse.imgCollapsePlay -> {
                play()
            }

            framePlayerCollapse.imgCollapseClose -> {
                close()
                hideTransitionWatching()
            }
        }
    }

    /**
     * Motions
     * */
    private fun showTransitionWatching() = with(vb) {
        when (root.currentState) {
            R.id.collapseWindow -> {
                root.transitionToStart()
            }

            R.id.fullWindow -> {
                frameWatching.root.transitionToState(R.id.start, 0)
                frameWatching.rcvWatching.scrollToPosition(0)

                root.setTransitionTo(R.id.normal, R.id.fullWindow, 300)
                root.setProgress(0.2F, 1.0F)
            }

            else -> {
                root.setTransition(R.id.transitionWatching)
                root.transitionToEnd()
            }
        }
    }

    private fun hideTransitionWatching() = with(vb) {
        root.setTransitionTo(R.id.collapseWindow, R.id.normal)
        root.transitionToStart()
    }

    private fun checkEnableTransition(id: Int) {
        vb.playerView.useController = id != R.id.collapseWindow
        enableTransitionCollapse(id)
    }

    private fun enableTransitionCollapse(id: Int) = with(vb) {
        logDebug("enableTransitionCollapse: ${id == R.id.fullWindow}")
        if (id == R.id.fullWindow) {
            root.setTransition(R.id.transitionCollapse)
        }
        root.enableViewTransition(R.id.transitionCollapse, id != R.id.normal)
        root.enableTransition(R.id.transitionCollapse, id != R.id.normal)
    }

    /**
     * Load videos
     * */
    private fun checkPermissionMedia() {
        val permissionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_VIDEO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permissionName
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadVideoMedia()
            }

            shouldShowRequestPermissionRationale(permissionName) -> {
                showToastShort("shouldShowRequestPermissionRationale")
            }

            else -> {
                requestPermissionLauncher.launch(permissionName)
            }
        }
    }

    private fun loadVideoMedia() {
        videosLocal = requireContext().applicationContext.loadMedia()
        adapterVideos.fillData(videosLocal)
    }

    /**
     * Play video
     * */
    private fun play() {
        if (player.isPlaying) {
            player.pause()
            vb.framePlayerCollapse.imgCollapsePlay.setImageResource(R.drawable.ic_play)
        } else {
            player.play()
            vb.framePlayerCollapse.imgCollapsePlay.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun close() {
        player.stop()
        player.clearMediaItems()
    }

    private fun prepareVideos(videos: MutableList<YouTubeForm>) {
        player.clearMediaItems()
        val mediaItems = videos.map { MediaItem.fromUri(it.uri) }
        player.addMediaItems(mediaItems)
        player.prepare()
        player.play()

        setupListVideoPlaylist(videos)
    }

    private fun setupListVideoPlaylist(watchingVideos: MutableList<YouTubeForm>) {
        tryCatch {
            val listPending = watchingVideos.toImmutableList().toMutableList()
            //listPending.removeFirst()
            adapterWatching.fillData(listPending)
        }
    }

    /**
     * Fill data
     * */
    private fun fillData(youtubeForm: YouTubeForm) {
        fillDataWatching(youtubeForm)
        fillDataPlayerCollapse(youtubeForm)
    }

    private fun fillDataWatching(youtubeForm: YouTubeForm) = with(vb) {
        frameWatching.tvWatchingTitle.text = youtubeForm.title
        frameWatching.tvWatchingAuthorName.text = youtubeForm.authorName.ifEmpty { "Author name" }
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

    private fun fillDataPlayerCollapse(youtubeForm: YouTubeForm) = with(vb) {
        framePlayerCollapse.tvPlayerCollapseTitle.text = youtubeForm.title
        framePlayerCollapse.tvPlayerCollapseDescribe.text =
            youtubeForm.authorName.ifEmpty { "Author name" }
        framePlayerCollapse.imgCollapsePlay.setImageResource(R.drawable.ic_pause)
    }
}