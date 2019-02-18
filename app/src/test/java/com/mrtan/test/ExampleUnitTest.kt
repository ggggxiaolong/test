package com.mrtan.test

import org.junit.Assert.assertEquals
import org.junit.Test
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.instacart.library.truetime.TrueTime
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.Locale

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 4)
  }
}

fun main(args: Array<String>) {
//  print(getAccessToken())
  val client = SntpClient()
  if (client.requestTime("time.apple.com", 10_000)) {
    val now = client.ntpTime + System.nanoTime() / 1000 - client.ntpTimeReference
    val current = Date(now)
    System.out.println(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(current))
  }
}

fun getAccessToken(): String {
  // -DsocksProxyHost=127.0.0.1 -DsocksProxyPort=1080
  val googleCredential = GoogleCredential
    .fromStream(FileInputStream("service-account.json"))
    .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
  googleCredential.refreshToken()
  return googleCredential.accessToken
}