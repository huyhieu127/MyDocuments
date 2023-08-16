package com.huyhieu.mydocuments.ui.fragments.my_tube.exo_player

import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentExoPlayerBinding


@UnstableApi
class ExoPlayerFragment : BaseFragment<FragmentExoPlayerBinding>() {
    // Registers a photo picker activity launcher in single-select mode.

    private val player by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uri ->
            if (uri != null) {
                Log.d(">>>>>>>>>>>>>>>", "Selected URI: $uri")

                val mediaItems = uri.map { MediaItem.fromUri(it) }
                player.addMediaItems(mediaItems)
                //Play
                player.prepare()
                player.play()
            }
        }

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        vb.playerView.player = player
        //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
        //findVideos()
    }

//    private fun findVideos() {
//        val cursor: Cursor?
//        var absolutePathOfImage: String? = null
//        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//        val projection = arrayOf(
//            MediaStore.MediaColumns.DATA,
//            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
//            MediaStore.Video.Media._ID,
//            MediaStore.Video.Thumbnails.DATA
//        )
//        val orderBy = MediaStore.Images.Media.DATE_TAKEN
//        cursor = requireContext().contentResolver.query(
//            uri, projection, null, null,
//            "$orderBy DESC"
//        )
//        val column_index_data: Int = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//        val column_index_folder_name: Int = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
//        val column_id: Int = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
//        val thum: Int = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)
//        while (cursor.moveToNext()) {
//            absolutePathOfImage = cursor.getString(column_index_data)
//            Log.e("Column", absolutePathOfImage)
//            Log.e("Folder", cursor.getString(column_index_folder_name))
//            Log.e("column_id", cursor.getString(column_id))
//            Log.e("thum", cursor.getString(thum))
//
//        }
//        cursor.close()
//
//        val projection = arrayOf(
//            MediaStore.MediaColumns.DATA,
//            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
//            MediaStore.Video.Media._ID,
//            MediaStore.Video.Thumbnails.DATA
//        )
//        val selection = sql-where-clause-with-placeholder-variables
//        val selectionArgs = values-of-placeholder-variables
//        val sortOrder = sql-order-by-clause
//
//        requireActivity().applicationContext.contentResolver.query(
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            selection,
//            selectionArgs,
//            sortOrder
//        )?.use { cursor ->
//            while (cursor.moveToNext()) {
//                // Use an ID column from the projection to get
//                // a URI representing the media item itself.
//            }
//        }
//    }
}