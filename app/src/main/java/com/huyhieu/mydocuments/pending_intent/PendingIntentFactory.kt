package com.huyhieu.mydocuments.pending_intent

import android.app.PendingIntent
import android.content.Context
import com.huyhieu.mydocuments.pending_intent.direction.CloudMessagingDirection
import com.huyhieu.mydocuments.pending_intent.direction.DefaultDirection

object PendingIntentFactory {
    fun direction(context: Context, type: String, data: String): PendingIntent {
        val target = getTargetDirection(type)
        return target.direction(context, data)
    }

    private fun getTargetDirection(type: String): PendingIntentDirection {
        return when (type) {
            "1" -> CloudMessagingDirection()
            else -> DefaultDirection()
        }
    }
}