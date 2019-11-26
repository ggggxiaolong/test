package com.mrtan.test.work

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class UpdateWork(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

  override fun doWork(): Result {
    for (i in 1..100){
      Thread.sleep(100)
      Timber.i(i.toString())
      setProgressAsync(Data.Builder().putInt("progress", i).build())
    }
    return Result.success()
  }
}