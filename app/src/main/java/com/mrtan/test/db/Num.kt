package com.mrtan.test.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.mrtan.test.db.Num.Companion.TABLE_NUM

/**
 * Mrtan created at 2018/6/25.
 */

@Entity(tableName = TABLE_NUM)
data class Num constructor(
    @PrimaryKey @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = VALUE) val value: Int
) {
  companion object {
    const val TABLE_NUM = "num"
    const val ID = "_id"
    const val VALUE = "value"
  }
}