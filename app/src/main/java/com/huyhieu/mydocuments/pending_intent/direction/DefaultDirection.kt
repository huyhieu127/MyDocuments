package com.huyhieu.mydocuments.pending_intent.direction

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.huyhieu.mydocuments.pending_intent.PendingIntentDirection
import com.huyhieu.mydocuments.ui.activities.main.MainActivity

class DefaultDirection : PendingIntentDirection {
    override fun direction(context: Context, data: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}