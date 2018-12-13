package com.mrtan.test

import org.junit.Assert.assertEquals
import org.junit.Test
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import java.io.FileInputStream
import java.util.Arrays

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
  print(getAccessToken())
}

fun getAccessToken(): String {
  // -DsocksProxyHost=127.0.0.1 -DsocksProxyPort=1080
  val googleCredential = GoogleCredential
    .fromStream(FileInputStream("service-account.json"))
    .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
  googleCredential.refreshToken()
  return googleCredential.accessToken
}