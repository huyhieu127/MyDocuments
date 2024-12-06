package com.huyhieu.mydocuments.pending_intent

import android.app.PendingIntent
import android.content.Context

interface PendingIntentDirection {
    fun direction(context: Context, data: String): PendingIntent
}