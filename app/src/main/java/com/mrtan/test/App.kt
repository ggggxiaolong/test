package com.mrtan.test

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Debug
import android.util.Log
//import com.google.firebase.FirebaseApp
import com.maning.librarycrashmonitor.MCrashMonitor
import com.maning.librarycrashmonitor.listener.MCrashCallBack
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.File

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    MCrashMonitor.init(this, true, object : MCrashCallBack {
      override fun onCrash(file: File) {
        //可以在这里保存标识，下次再次进入把日志发送给服务器
        Log.i("creash", "CrashMonitor回调:" + file.getAbsolutePath())
      }
    })
    if (BuildConfig.DEBUG)
      Timber.plant(DebugTree())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // Create channel to show notifications.
      val channelId = getString(R.string.default_notification_channel_id)
      val channelName = getString(R.string.default_notification_channel_name)
      val notificationManager = getSystemService(NotificationManager::class.java)
      notificationManager?.createNotificationChannel(
          NotificationChannel(
              channelId,
              channelName, NotificationManager.IMPORTANCE_LOW
          )
      )
    }
  }
}