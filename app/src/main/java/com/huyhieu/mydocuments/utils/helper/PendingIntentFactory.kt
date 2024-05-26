package com.huyhieu.mydocuments.utils.helper

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavDeepLinkBuilder
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.ui.activities.main.MainActivity

object PendingIntentFactory {
    fun direction(context: Context, type: String, data: String): PendingIntent {
        return when (type) {
            "1" -> CloudMessagingDirection().direction(context, data)
            else -> DefaultDirection().direction(context, data)
        }
    }
}

interface PendingIntentDirection {
    fun direction(context: Context, data: String): PendingIntent
}

class DefaultDirection : PendingIntentDirection {
    override fun direction(context: Context, data: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}

class CloudMessagingDirection : PendingIntentDirection {
    override fun direction(context: Context, data: String): PendingIntent {
        val intent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main_graph)
            .setDestination(R.id.cloudMessagingFragment)
            .setArguments(
                Bundle().apply {
                    putString("data", data)
                }
            )
            .createPendingIntent()
        return intent
    }
}