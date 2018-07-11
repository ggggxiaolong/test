package com.mrtan.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.MediaController
import android.widget.VideoView

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
    video.setMediaController(controller)
    video.setVideoPath("android.resource://$packageName/${R.raw.test}")
    video.start()
    video.setOnPreparedListener {
      it.start()
      it.isLooping = true
    }
  }

}