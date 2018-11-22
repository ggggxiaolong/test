package com.mrtan.test

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import org.junit.Test

class SerializeTest {

  @Serializable
  data class Data(val a: Int, @Optional val b: String = "er", @Transient val c: Int = 5, @SerialName("d") val dd: Float = 7.1f)

  @Test fun testSerialize(){
    System.out.println(JSON.stringify(Data.serializer(), Data(42)))
    System.out.println(JSON.indented.stringify(Data.serializer().list, listOf(Data(42))))
    val obj = JSON.unquoted.parse(Data.serializer(), "{a:2, d: 3.4}")
    System.out.print(obj)

    /*
    {"a":42,"b":"er","d":7.1}  // 默认格式
    [
        {
            "a": 42,
            "b": "er",
            "d": 7.1
        }
    ]// 缩进格式
    Data(a=2, b=er, c=5, dd=3.4)
     */
  }
}