package com.mrtan.test.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Mrtan created at 2018/6/25.
 */

@Database(
    entities = [Num::class],
    version = 1
)
abstract class AppDataBase: RoomDatabase() {
  abstract fun numDao(): NumDao
}