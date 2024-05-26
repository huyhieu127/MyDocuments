package com.huyhieu.mydocuments.utils.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.libraries.utils.logDebug


class NotificationHelper(private val context: Context) {


    fun showNotification(data: MutableMap<String, String>) {
        // Xử lý payload dữ liệu
        val title = data["title"].orEmpty()
        val content = data["content"].orEmpty()
        val type = data["type"].orEmpty()
        val imageUrl = data["image_url"].orEmpty()
        val promotionId = data["promotionId"].orEmpty() // Others data

        val soundUri = Settings.System.DEFAULT_NOTIFICATION_URI
        logDebug("showNotification: defaultSoundUri: $soundUri")
        val pendingIntent = PendingIntentFactory.direction(context, type, promotionId)
        if (imageUrl.isNotEmpty()) {
            //Load LargeIcon from remote
            Glide.with(context).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    handleNotificationManager(
                        context, title, content, soundUri, resource, pendingIntent
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        } else {
            val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_car_red)
            handleNotificationManager(context, title, content, soundUri, largeIcon, pendingIntent)
        }

    }

    private fun handleNotificationManager(
        context: Context,
        title: String,
        content: String,
        defaultSoundUri: Uri,
        largeIcon: Bitmap,
        pendingIntent: PendingIntent
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Biểu tượng đơn sắc
            .setLargeIcon(largeIcon) // Biểu tượng lớn đầy đủ màu sắc
            .setContentTitle(title).setContentText(content).setContentIntent(pendingIntent)
            //.setSound(defaultSoundUri)
            .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_SOUND)
        //.setDefaults(NotificationCompat.DEFAULT_ALL)

        //Set progress
//            .setOngoing(true)
//            .setProgress(100, 50, false)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.apply {
            createChannelForSinceApi26()// Available since API 26
            notify(0, builder.build())
        }
    }

    private fun NotificationManager.createChannelForSinceApi26() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            this.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "promotion_channel_id"
        private const val CHANNEL_NAME = "Khuyến mãi"
        private const val CHANNEL_DESCRIPTION = "Thông tin các siêu khuyến mãi"
    }
}
