package com.mrtan.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.*

class AnkoActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    verticalLayout {
      padding = dip(30)
      editText {
        hint = "Name"
        textSize = 24f
      }
      editText {
        hint = "Password"
        textSize = 24f
      }
      button("Login") {
        textSize = 26f
      }
    }
  }
}