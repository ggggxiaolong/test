package com.mrtan.test

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

fun main() = {
  val job = Job()
  runBlocking(job){
    val job = CompletableDeferred<Any>()
    job.invokeOnCompletion {
      println(job.isCancelled)
      println("finish")
    }
    job.cancel()
  }



}
// api service
fun api(): Deferred<String> = CompletableDeferred()

class Activity(): CoroutineScope {
  val job = Job()
  override val coroutineContext: CoroutineContext = Dispatchers.Default + job

  fun runApi(){
    launch {
      val deferred = api()
      job.invokeOnCompletion {
        if (job.isCancelled)
          deferred.cancel()
      }
      try{
        val result = deferred.await()
        //todo
      } catch (t: Throwable){
        // exception control
      }
    }
  }

  fun destory(){
    job.cancel()
  }

}