package com.mrtan.test

//import net.idik.lib.cipher.so.CipherClient
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mrtan.test.ui.CameraActivity
import com.mrtan.test.work.UpdateWork
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

  val message = MutableLiveData<String>()

  @SuppressLint("MissingPermission")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    message.observe(this, Observer {
      text.text = it ?: "loading"
    })
    button.setOnClickListener {
    }
    imageView.setImageDrawable(
      TextDrawable.builder().buildRound("A", Color.RED)
    )
    val progressReq = PeriodicWorkRequest.Builder(UpdateWork::class.java, 15 , TimeUnit.MINUTES).addTag("progress").build();
    WorkManager.getInstance(this).enqueueUniquePeriodicWork("update", ExistingPeriodicWorkPolicy.KEEP, progressReq)
    ObjectAnimator.ofFloat(loadView, "rotation", 0f, 360f).apply {
      duration = 1000
      repeatMode = ValueAnimator.RESTART
      interpolator = LinearInterpolator()
      repeatCount = -1
      start()
    }
    text.text = Html.fromHtml("<a href=\"https://developer.android.com\">Click here for more.</a>")
    text.movementMethod = LinkMovementMethod.getInstance()
    (button.icon as Animatable).start()
    download.setOnClickListener {
      startActivity(Intent(this, CameraActivity::class.java))
    }
    WorkManager.getInstance(this).getWorkInfoByIdLiveData(progressReq.id).observe(this, Observer { info ->
      progress.progress = info.progress.getInt("progress", 0)
    })
  }

}