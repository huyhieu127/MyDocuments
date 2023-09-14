package com.huyhieu.mydocuments.ui.fragments.tiktok

import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheKeyFactory
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.upstream.DefaultAllocator
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.ItemTikTokPlayerBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.models.TikTokVideoForm
import com.huyhieu.mydocuments.utils.delayed
import com.huyhieu.mydocuments.utils.tryCatch
import java.io.File

@UnstableApi
class TikTokPlayerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listVideo = listOf<TikTokVideoForm>()

    private val cacheSize: Long = 100 * 1024 * 1024 //100MB
    private val leastRecentlyUsedCacheEvictor by lazy { LeastRecentlyUsedCacheEvictor(cacheSize) }
    private val databaseProvider by lazy { StandaloneDatabaseProvider(context) }
    private val simpleCache by lazy {
        SimpleCache(
            File(context.cacheDir, "tiktok_video"), leastRecentlyUsedCacheEvictor, databaseProvider
        )
    }
    private val cacheFactory by lazy {
        val httpDataSourceFactory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)

        val defaultDataSourceFactory = DefaultDataSource.Factory(context, httpDataSourceFactory)

        CacheDataSource.Factory().apply {
            cacheKeyFactory = CacheKeyFactory.DEFAULT
            setCache(simpleCache)
            setUpstreamDataSourceFactory(defaultDataSourceFactory)
            setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }
    }

    var storedMedias: SparseArray<String> = SparseArray()
    var storedPlayers: SparseArray<ExoPlayer> = SparseArray()
    var storedPlayerView: SparseArray<PlayerView> = SparseArray()

    @SuppressLint("NotifyDataSetChanged")
    fun fillData(listVideo: List<TikTokVideoForm>) {
        this.listVideo = listVideo
        notifyDataSetChanged()
    }

    fun releaseAllPlayer() {
        tryCatch {
            this.storedPlayers.forEach { _, value ->
                value.release()
            }
            simpleCache.release()
        }
    }

    fun playVideo(position: Int) {
        tryCatch {
            storedPlayers[position].play()
        }
    }

    fun pauseVideo(position: Int) {
        tryCatch {
            storedPlayers[position].pause()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bind =
            ItemTikTokPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TikTokPlayerVH(bind)
    }

    override fun getItemCount(): Int = if (listVideo.isEmpty()) 3 else listVideo.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listVideo.isNotEmpty() && holder is TikTokPlayerVH) {
            holder.bindViewHolder()
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (listVideo.isNotEmpty() && holder is TikTokPlayerVH) {
            with(holder.vb) {
                val position = holder.layoutPosition

                val playerView = storedPlayerView.get(position)
                val player = storedPlayers.get(position)
                val urlMediaItem = storedMedias.get(position)

                //viewLoading.root.isVisible = true
                framePreview.addView(playerView)
                playerView.isInvisible = true

                playerView.player = player
                player.playVideo(urlMediaItem)

            }
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (listVideo.isNotEmpty() && holder is TikTokPlayerVH) {
            with(holder.vb) {
                val position = holder.layoutPosition
                val indexOfChild = framePreview.indexOfChild(storedPlayerView.get(position))
                if (indexOfChild >= 0) {
                    framePreview.removeViewAt(indexOfChild)
                    storedPlayers.get(position).stop()
                }
            }
        }
    }

    inner class TikTokPlayerVH(val vb: ItemTikTokPlayerBinding) : RecyclerView.ViewHolder(vb.root) {

        private val playerCallback = object : Player.Listener {

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                if (!isLoading) {
                    delayed {
                        //vb.viewLoading.root.isVisible = false
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    tryCatch {
                        storedPlayerView[layoutPosition].isVisible = true
                        delayed {
                            //vb.viewLoading.root.isVisible = false
                        }
                    }
                }
            }
        }

        fun bindViewHolder() {
            val video = listVideo[layoutPosition]
            storedMedias.append(layoutPosition, video.url)
            storedPlayerView.append(layoutPosition, getVideoSource())
            storedPlayers.append(layoutPosition, getPlayer(playerCallback))

            with(vb.frameController) {
                imgWhiteHeart.setOnClickListener {
                    imgLottieHeart.apply {
                        isVisible = true
                        scaleX = 1F
                        scaleY = 1F
                        playAnimation()
                    }
                }
                imgLottieHeart.setOnClickListener {
                    imgLottieHeart.apply {
                        animate().scaleX(0F).scaleY(0F).withEndAction {
                            isInvisible = true
                        }
                    }
                }
            }
        }


        private fun getVideoSource(): PlayerView {
            return PlayerView(context).apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                useController = false
                setBackgroundColor(context.color(R.color.white))
                setShutterBackgroundColor(context.color(R.color.white))
                //LOADING DEFAULT
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            }
        }

        private fun getPlayer(playerCallback: Player.Listener): ExoPlayer {
            val loadControl = DefaultLoadControl.Builder().setAllocator(DefaultAllocator(true, 16))
                .setBufferDurationsMs(
                    VideoPlayerConfig.MIN_BUFFER_DURATION,
                    VideoPlayerConfig.MAX_BUFFER_DURATION,
                    VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                    VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER
                ).setTargetBufferBytes(-1).setPrioritizeTimeOverSizeThresholds(true).build()
            return ExoPlayer.Builder(context).setLoadControl(loadControl)
                .setMediaSourceFactory(DefaultMediaSourceFactory(cacheFactory)).build().apply {
                    playWhenReady = true
                    repeatMode = Player.REPEAT_MODE_ONE
                    addListener(playerCallback)
                }
        }
    }

    private fun ExoPlayer.playVideo(url: String) {
        val mediaSource: MediaSource =
            ProgressiveMediaSource.Factory(cacheFactory).createMediaSource(MediaItem.fromUri(url))
        setMediaSource(mediaSource, true)
        prepare()
    }

    object VideoPlayerConfig {
        //Minimum Video you want to buffer while Playing
        const val MIN_BUFFER_DURATION = 2000

        //Max Video you want to buffer during PlayBack
        const val MAX_BUFFER_DURATION = 5000

        //Min Video you want to buffer before start Playing it
        const val MIN_PLAYBACK_START_BUFFER = 1500

        //Min video You want to buffer when user resumes video
        const val MIN_PLAYBACK_RESUME_BUFFER = 2000
    }
}