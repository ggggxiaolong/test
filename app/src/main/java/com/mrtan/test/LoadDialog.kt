package com.mrtan.test

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ConvertUtils

class LoadDialog(context: Context) : AlertDialog(context) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
    (view.findViewById<ImageView>(R.id.progress).drawable as Animatable).start()
    setContentView(view)
    val p = window!!.attributes
    p.width = ConvertUtils.dp2px(140f)
    window!!.attributes = p
  }

}