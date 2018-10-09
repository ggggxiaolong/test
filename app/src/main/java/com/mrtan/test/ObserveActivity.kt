package com.mrtan.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.room.Room
import com.mrtan.test.db.AppDataBase
import com.mrtan.test.db.Num
import kotlinx.android.synthetic.main.activity_observe.*
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

/**
 * Mrtan created at 2018/6/25.
 */
class ObserveActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_observe)
    val dataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "test").build()
    GlobalScope.launch {
      dataBase.numDao().insert(Num(1, 0))
    }
    dataBase.numDao().getNum(1).observe(this, Observer { num ->
      kotlin.run { if (num != null) text.text = num.value.toString() }
    })
    var value = 0
    add.setOnClickListener {
      value += 1
      GlobalScope.launch {
        dataBase.numDao().update(Num(1, value))
      }
    }
  }
}