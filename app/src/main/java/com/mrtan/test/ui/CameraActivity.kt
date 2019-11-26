package com.mrtan.test.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.Size
import android.view.Surface
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mrtan.test.R
import kotlinx.android.synthetic.main.activity_camera.*
import org.jetbrains.anko.toast
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class CameraActivity : AppCompatActivity() {
  private val REQUEST_CODE_PERMISSION = 10
  private val REQUEST_PERMISSION = arrayOf(Manifest.permission.CAMERA)
  private val excutor = Executors.newSingleThreadExecutor()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_camera)
    if (allPermissionsGranted()) {
      view_finder.post { startCamera() }
    } else {
      ActivityCompat.requestPermissions(this, REQUEST_PERMISSION, REQUEST_CODE_PERMISSION)
    }
    view_finder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateTransform() }
  }

  private fun startCamera() {
    val previewConfig = PreviewConfig.Builder().apply {
      setTargetResolution(Size(320, 320))
    }.build()
    val preview = Preview(previewConfig)
    preview.setOnPreviewOutputUpdateListener {
      val parent = view_finder.parent as ViewGroup
      parent.removeView(view_finder)
      parent.addView(view_finder, 0)
      view_finder.surfaceTexture = it.surfaceTexture
      updateTransform()
    }
    val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
      setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
    }.build()
    val imageCapture = ImageCapture(imageCaptureConfig)
    capture.setOnClickListener {
      val file = File(externalCacheDirs.first(), "${System.currentTimeMillis()}.jpg")
      imageCapture.takePicture(file, excutor, object : ImageCapture.OnImageSavedListener{
        override fun onImageSaved(file: File) {
          val msg = "Photo capture succeeded: ${file.absolutePath}"
          Timber.i(msg)
          view_finder.post {
            toast(msg)
          }
        }

        override fun onError(imageCaptureError: ImageCapture.ImageCaptureError, message: String, cause: Throwable?) {
          val msg = "Photo capture failed: $message"
          Timber.i(msg)
          view_finder.post {
            toast(msg)
          }
        }

      })
    }
    CameraX.bindToLifecycle(this, preview, imageCapture)
  }

  private fun updateTransform() {
    val matrix = Matrix()
    val centerX = view_finder.width / 2f
    val centerY = view_finder.height / 2f

    val rotationDegree = when (view_finder.display.rotation) {
      Surface.ROTATION_0 -> 0
      Surface.ROTATION_90 -> 90
      Surface.ROTATION_180 -> 180
      Surface.ROTATION_270 -> 270
      else -> return
    }
    matrix.postRotate(rotationDegree.toFloat(), centerX, centerY)
    view_finder.setTransform(matrix)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == REQUEST_CODE_PERMISSION) {
      if (allPermissionsGranted()) {
        view_finder.post { startCamera() }
      } else {
        toast("Permission not granted by the user")
        finish()
      }
    }
  }

  private fun allPermissionsGranted() = REQUEST_PERMISSION.all {
    ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
  }
}