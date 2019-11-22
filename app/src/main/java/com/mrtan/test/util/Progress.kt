package com.mrtan.test.util

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.*
import kotlin.jvm.JvmStatic

@Throws(Exception::class)
fun run(path: String) {
    val request = Request.Builder()
            .url(path)
            .build()

    val progressListener = object : ProgressListener {
        override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
            println(bytesRead)
            println(contentLength)
            println(done)
            System.out.format("%d%% done\n", 100 * bytesRead / contentLength)
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
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
//        println("download finish")
    }
    Thread.sleep(2000)
}

private class ProgressResponseBody internal constructor(private val responseBody: ResponseBody, private val progressListener: ProgressListener) : ResponseBody() {
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

fun main(args: Array<String>) {
    run("https://github-production-release-asset-2e65be.s3.amazonaws.com/130644624/e3975c00-a69b-11e9-95f6-a5d5f2ba080b?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20190823%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20190823T030904Z&X-Amz-Expires=300&X-Amz-Signature=dd697021c5917f83d3a73ffbc3f75fde5135cbe471c617036b0b7d312665f670&X-Amz-SignedHeaders=host&actor_id=10071089&response-content-disposition=attachment%3B%20filename%3DBND1-v4.0.0-linux.zip&response-content-type=application%2Foctet-stream")
}