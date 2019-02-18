package com.mrtan.test

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.imageView
import kotlinx.android.synthetic.main.activity_main.text
import timber.log.Timber

class MainActivity : AppCompatActivity() {
  val message = MutableLiveData<String>()

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
    val url = "https://developer.android.com"
    spannableString.setSpan(URLSpan (url), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text.text = spannableString
    text.movementMethod = LinkMovementMethod.getInstance()
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