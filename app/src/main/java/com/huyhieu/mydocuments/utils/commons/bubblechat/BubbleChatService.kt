package com.huyhieu.mydocuments.utils.commons.bubblechat

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager

class BubbleChatService : Service() {
    private var mWindowManager: WindowManager? = null
    private var mChatHeadView: BubbleView? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //Inflate the chat head layout we created
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mChatHeadView = BubbleView(mWindowManager!!, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mChatHeadView != null) mWindowManager!!.removeView(mChatHeadView)
    }
}