package com.mrtan.test.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CloudMessageService: FirebaseMessagingService() {

  override fun onMessageReceived(remoteMessage: RemoteMessage?) {
    super.onMessageReceived(remoteMessage)
    Log.d(TAG, "From: ${remoteMessage?.from}")

    // Check if message contains a data payload.
    remoteMessage?.data?.isNotEmpty()?.let {
      Log.d(TAG, "Message data payload: " + remoteMessage.data)
    }

    // Check if message contains a notification payload.
    remoteMessage?.notification?.let {
      Log.d(TAG, "Message Notification Body: ${it.body}")
    }

  }

  override fun onNewToken(p0: String?) {
    super.onNewToken(p0)
    Log.i("New Token", p0)
  }

  companion object {
    private val TAG = "FirebaseMsgService"
  }
}