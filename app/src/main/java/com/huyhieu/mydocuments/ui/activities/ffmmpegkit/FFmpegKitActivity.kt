package com.huyhieu.mydocuments.ui.activities.ffmmpegkit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.Level
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityFfmpegKitBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FFmpegKitActivity : BaseActivity<ActivityFfmpegKitBinding>() {

    private val chooseVideoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                //UI
                val selectedUri = activityResult.data?.data
                // OI FILE Manager
                val fileManagerString = selectedUri?.path
                // MEDIA GALLERY
                val selectedPath = getPath(selectedUri)
                vb.run {
                    tvFilePath.text = selectedPath ?: ""
                    val folder = selectedPath!!.substring(
                        0,
                        selectedPath.lastIndexOf("/") + 1
                    ) + "FFmpeg_${System.currentTimeMillis()}.mp3"
                    edtFolder.setText(folder)
                    edtFolder.requestFocus(folder.length)
                }
            }
        }

    override fun binding() = ActivityFfmpegKitBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        vb.run {
            cvChooseFile.setOnClickListener(this@FFmpegKitActivity)
            btnConvert.setOnClickListener(this@FFmpegKitActivity)
        }
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
    }

    override fun onClick(p0: View?) {
        vb.run {
            when (p0?.id) {
                cvChooseFile.id -> showIntentChooseVideo()
                btnConvert.id -> convertFile()
            }
        }
    }

    private fun convertFile() {
        //Xóa âm
        //val cmd = "-i ${mBinding.tvFilePath.text} -an ${mBinding.edtFolder.text}"
        //Thay đổi độ phân giải
        //val cmd = "-i ${mBinding.tvFilePath.text} -s 1280x720 -c:a copy ${mBinding.edtFolder.text}"
        //Giảm chất lượng video
        val cmd =
            "-i ${vb.tvFilePath.text} -vn ${vb.edtFolder.text}"
        val result = StringBuilder("Result: \n")
        FFmpegKit.executeAsync(cmd, { session ->
            val state = session.state
            val returnCode = session.returnCode
            // CALLED WHEN SESSION IS EXECUTED
            result.append(
                String.format(
                    "\n\n\n> FFmpeg process exited with state %s and rc %s.%s",
                    state,
                    returnCode,
                    session.failStackTrace
                )
            )
            CoroutineScope(Dispatchers.Main).launch {
                vb.run {
                    tvResult.text = "$result\n\n\n"
                    nsv.smoothScrollTo(0, nsv.getChildAt(0).height + 200)
                }
            }
        }, { log ->
            if (log.level != Level.AV_LOG_INFO || log.message.contains("size")) {
                result.append("\n\n> ").append(log.message)
                CoroutineScope(Dispatchers.Main).launch {
                    vb.run {
                        tvResult.text = "$result\n\n\n"
                        nsv.smoothScrollTo(0, nsv.getChildAt(0).height + 200)
                    }
                }
            }
        }, { statistics ->
            logDebug("statistics: ${statistics.size.toString()}")
        })
    }

    private fun showIntentChooseVideo() {
        Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
            type = "video/*"
        }.run {
            chooseVideoLauncher.launch(Intent.createChooser(this, "Chọn video phù hợp"))
        }
    }

    private fun getPath(uri: Uri?): String? {
        uri?.let {
            val projection = arrayOf(
                MediaStore.Video.Media.DATA
            )
//            val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
//            cursor?.let {
//                val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
//                cursor.moveToFirst()
//                return cursor.getString(columnIndex) ?: "null"
//            }
            contentResolver?.query(uri, projection, null, null, null)?.use { cursor ->
                while (cursor.moveToNext()) {
                    val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
                    return cursor.getString(columnIndex)
                }
            }
        }
        return null
    }
}