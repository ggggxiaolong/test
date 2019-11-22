package com.mrtan.test

//import net.idik.lib.cipher.so.CipherClient
import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.iid.FirebaseInstanceId
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.lang.Runnable
import java.security.Permission

class MainActivity : AppCompatActivity() {

  val message = MutableLiveData<String>()

  @SuppressLint("MissingPermission")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    message.observe(this, Observer {
      text.text = it ?: "loading"
    })
    Thread(Runnable { getToken() }).start()
    button.setOnClickListener {
    }
    imageView.setImageDrawable(
      TextDrawable.builder().buildRound("A", Color.RED)
    )
    val spannableString = SpannableString("Click here for more.")
//    val url = "https://developer.android.com"
//    spannableString.setSpan(URLSpan (url), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//    text.text = spannableString
    ObjectAnimator.ofFloat(loadView, "rotation", 0f, 360f).apply {
      duration =1000
      repeatMode = ValueAnimator.RESTART
      interpolator = LinearInterpolator()
      repeatCount = -1
      start()
    }
    text.text = Html.fromHtml("<a href=\"https://developer.android.com\">Click here for more.</a>")
    text.movementMethod = LinkMovementMethod.getInstance()
    (button.icon as Animatable).start()
    download.setOnClickListener {
//      progress.max = 100
//      progress.progress = 0
//      AndPermission.with(this)
//        .runtime()
//        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        .onGranted {
//          GlobalScope.launch {
//            downloadFile("https://publicobject.com/helloworld.txt", object : DownloadCallback {
//              override fun onProgress(p: Int) {
//                runOnUiThread{
//                  progress.progress = p
//                }
//              }
//
//              override fun onSuccess(file: File) {
//                ToastUtils.showShort("download success")
//              }
//
//              override fun onFail(error: Int) {
//                ToastUtils.showShort("download fail $error")
//              }
//
//            }, File(Environment.getExternalStoragePublicDirectory(
//              Environment.DIRECTORY_DOWNLOADS
//            ), "okhttp.txt"))
//          }
//        }.start()
    }
  }

  private fun getToken() {
    try {
      val token = FirebaseInstanceId.getInstance()
        .getToken("freemrtan@gmai.com", "android")
      Timber.i("Token %s", token)
    } catch (t: Throwable) {
      Timber.e(t)
    }
  }
}