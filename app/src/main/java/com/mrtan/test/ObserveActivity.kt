package com.mrtan.test

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mrtan.test.db.AppDataBase
import com.mrtan.test.db.Num
import kotlinx.android.synthetic.main.activity_observe.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

/**
 * Mrtan created at 2018/6/25.
 */
class ObserveActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_observe)
    val dataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "test").build()
    launch(CommonPool) {
      dataBase.numDao().insert(Num(1, 0))
    }
    dataBase.numDao().getNum(1).observe(this, Observer { num ->
      kotlin.run { if (num != null) text.text = num.value.toString() }
    })
    var value = 0
    add.setOnClickListener {
      value += 1
      launch(CommonPool) {
        dataBase.numDao().update(Num(1, value))
      }
    }
  }
}