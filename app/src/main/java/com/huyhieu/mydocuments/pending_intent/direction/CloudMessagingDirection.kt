package com.huyhieu.mydocuments.pending_intent.direction

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.navigation.NavDeepLinkBuilder
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.pending_intent.PendingIntentDirection


class CloudMessagingDirection : PendingIntentDirection {
    override fun direction(context: Context, data: String): PendingIntent {
        val intent = NavDeepLinkBuilder(context).setGraph(R.navigation.main_graph)
            .setDestination(R.id.cloudMessagingFragment).setArguments(Bundle().apply {
                putString("data", data)
            }).createPendingIntent()
        return intent
    }
}