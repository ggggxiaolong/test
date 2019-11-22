package com.mrtan.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_webview.webView

class WebViewActivity: AppCompatActivity(){
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview)
    webView.settings.javaScriptEnabled = true
    webView.loadUrl("https://introjs.com/")
  }
}