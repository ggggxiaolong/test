package com.mrtan.test.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.mrtan.test.db.Num.Companion.TABLE_NUM

/**
 * Mrtan created at 2018/6/25.
 */
@Dao
interface NumDao {
  @Query(value = "select * from $TABLE_NUM where ${Num.ID} = :id")
  fun getNum(id: Int): LiveData<Num>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(num: Num)

  @Update
  fun update(num: Num)

}