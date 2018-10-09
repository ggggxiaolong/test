package com.mrtan.test.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Mrtan created at 2018/6/25.
 */

@Database(
    entities = [Num::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
  abstract fun numDao(): NumDao
}