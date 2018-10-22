package com.mrtan.test

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.text
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainActivity : AppCompatActivity() {
  val message = MutableLiveData<String>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    message.observe(this, Observer {
      text.text = it ?: "loading"
    })
    GlobalScope.launch {
      SystemClock.sleep(10000)
      withContext(Main) {
        message.value = "hello"
      }
    }
    Thread(Runnable { getToken()  }).start()
  }

  private fun getToken(){
    try {
      val token = FirebaseInstanceId.getInstance()
        .getToken("freemrtan@gmai.com", "android")
      Timber.i("Token %s", token)
    } catch (t: Throwable){
      Timber.e(t)
    }
  }
}