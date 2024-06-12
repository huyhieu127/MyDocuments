package com.huyhieu.mydocuments.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.utils.helper.NotificationHelper

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val notificationHelper by lazy { NotificationHelper(baseContext) }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Xử lý tin nhắn nhận được
        logDebug("From: ${remoteMessage.from}", TAG)

        // Kiểm tra xem tin nhắn có chứa dữ liệu không.
        remoteMessage.data.isNotEmpty().let {
            logDebug("Message data payload: ${remoteMessage.data}", TAG)
            notificationHelper.showNotification(remoteMessage.data)
        }

        // Kiểm tra xem tin nhắn có chứa notification không.
        remoteMessage.notification?.let {
            logDebug("Message Notification Body: ${it.body}", TAG)
            // Xử lý notification
        }
    }

    override fun onNewToken(token: String) {
        logDebug("Refreshed token: $token", TAG)
        // Gửi token mới đến server backend của bạn
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        logDebug("Sending token to server: $token", TAG)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}