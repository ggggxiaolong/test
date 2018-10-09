package com.mrtan.test

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {
  lateinit var video: VideoView
  override fun onCreate(
    savedInstanceState: Bundle?
  ) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_video)
    video = findViewById(R.id.video)
    val controller = MediaController(this)
    controller.visibility = View.INVISIBLE
    /*video.setMediaController(controller)
    video.setVideoPath("android.resource://$packageName/${R.raw.test}")
    video.start()
    video.setOnPreparedListener {
      it.start()
      it.isLooping = true
    }*/
  }

  override fun onResume() {
    super.onResume()
    val thread = HandlerThread("back").apply { start() }
    val handler = object : Handler(thread.looper) {
      override fun handleMessage(msg: Message) {
        when (msg.what) {
          1 -> Log.i("tag", "start")
          2 -> Log.i("tag", "end")
        }
      }
    }
    handler.sendEmptyMessage(1)
    handler.post { SystemClock.sleep(5000) }
    handler.sendEmptyMessage(2)
  }
}