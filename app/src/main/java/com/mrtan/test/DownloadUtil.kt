package com.mrtan.test

import android.Manifest
import androidx.annotation.RequiresPermission
import okhttp3.*
import okio.*
import java.io.File
import java.io.IOException

interface DownloadCallback {
  fun onProgress(progress: Int)
  fun onSuccess(file: File)
  fun onFail(error: Int)
}

@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
fun downloadFile(url: String, callback: DownloadCallback, destFile: File) {
  val request = Request.Builder().url(url).build()
  val progressListener = object : ProgressListener {
    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
      callback.onProgress((100 * bytesRead / contentLength).toInt())
    }
  }

  val client = OkHttpClient.Builder()
    .addNetworkInterceptor(object : Interceptor {
      @Throws(IOException::class)
      override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
          .body(ProgressResponseBody(originalResponse.body!!, progressListener))
          .build()
      }
    })
    .build()

  client.newCall(request).execute().use { response ->
    if (!response.isSuccessful) {
      callback.onFail(response.code)
    } else {
      if (!destFile.exists()) destFile.createNewFile()
      val sink = destFile.sink().buffer()
      sink.writeAll(response.body!!.source())
      sink.close()
      callback.onSuccess(destFile)
    }
  }
}

class ProgressResponseBody internal constructor(private val responseBody: ResponseBody, private val progressListener: ProgressListener) : ResponseBody() {
  private var bufferedSource: BufferedSource? = null

  override fun contentType(): MediaType? {
    return responseBody.contentType()
  }

  override fun contentLength(): Long {
    return responseBody.contentLength()
  }

  override fun source(): BufferedSource {
    if (bufferedSource == null) {
      bufferedSource = source(responseBody.source()).buffer()
    }
    return bufferedSource!!
  }

  private fun source(source: Source): Source {
    return object : ForwardingSource(source) {
      var totalBytesRead = 0L

      @Throws(IOException::class)
      override fun read(sink: Buffer, byteCount: Long): Long {
        val bytesRead = super.read(sink, byteCount)
        // read() returns the number of bytes read, or -1 if this source is exhausted.
        totalBytesRead += if (bytesRead != -1L) bytesRead else 0
        progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
        return bytesRead
      }
    }
  }
}

internal interface ProgressListener {
  fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}