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
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mrtan.test.ui.CameraActivity
import com.mrtan.test.work.UpdateWork
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
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
    val progressReq = PeriodicWorkRequest.Builder(UpdateWork::class.java, 15, TimeUnit.MINUTES).addTag("progress").build();
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
    dateTime.filters = arrayOf(object : InputFilter {
      /**
       * yyyy-MM-dd HH:mm:ss
       * @param source 输入的文字
       * @param start 输入-0，删除-0
       * @param end 输入-文字的长度，删除-0
       * @param dest 原先显示的内容
       * @param dstart 输入-原光标位置，删除-光标删除结束位置
       * @param dend  输入-原光标位置，删除-光标删除开始位置
       * @return
       */
      override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        if (source == null || end == 0) return null //正常删除
        val sb = SpannableStringBuilder()
        val r = when (dstart) {
          in (0..3) -> {
            when (end + dstart) {
              in (0..3) -> sb.append(source)
              else -> {
                sb.append(source.subSequence(0, 4 - dstart)).append("-")
              }
            }
            sb
          }
          4 -> "-"
          in (5..6) -> {
            when (end + dstart) {
              6 -> sb.append(source)
              else -> {
                sb.append(source.subSequence(0, 7 - dstart)).append("-")
              }
            }
            return sb
          }
          7 -> "-"
          in (8..9) -> {
            when (end + dstart) {
              9 -> sb.append(source)
              else -> {
                sb.append(source.subSequence(0, 10 - dstart)).append(" ")
              }
            }
            return sb
          }
          10 -> " "
          in (11..12) -> {
            when (end + dstart) {
              12 -> sb.append(source)
              else -> {
                sb.append(source.subSequence(0, 13 - dstart)).append(":")
              }
            }
            return sb
          }
          13 -> ":"
          in (14..15) -> {
            when (end + dstart) {
              15 -> sb.append(source)
              else -> {
                sb.append(source.subSequence(0, 16 - dstart)).append(":")
              }
            }
            sb
          }
          in (17..18) -> {
            when (end + dstart) {
              18 -> sb.append(source)
              else -> {
                sb.append(source.subSequence(0, 19 - dstart))
              }
            }
            sb
          }
          else -> null
        }
        return r
      }
    }, InputFilter.LengthFilter(19))

  }

}