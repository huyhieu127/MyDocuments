package com.huyhieu.mydocuments.ui.activities.bubblechat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.huyhieu.library.extensions.showToastShort
import com.huyhieu.mydocuments.App
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityBubbleChatBinding

class BubbleChatActivity : BaseActivity<ActivityBubbleChatBinding>() {
    override fun initializeBinding() = ActivityBubbleChatBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        App.heightStatusBar =
            (resources.displayMetrics.heightPixels - (resources.displayMetrics.heightPixels - vb.viewAnchor.y)).toInt()
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
        vb.btnOpenBubbleChat.setOnClickListener {
            checkChatHeadPermission()
        }
    }

    override fun onClick(v: View?) {
    }

    private fun checkChatHeadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 218)
        } else {
            showBubbleChat()
        }
    }

    private fun showBubbleChat() {
        val intent =
            Intent(this, com.huyhieu.library.commons.bubblechat.BubbleChatService::class.java)
        startService(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 218) {
            //Check if the permission is granted or not.
            if (resultCode == Activity.RESULT_OK) {
                showToastShort("Permission ok!")
                showBubbleChat()
            } else { //Permission is not available
                showToastShort("Draw over other app permission not available. Closing the application")
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}