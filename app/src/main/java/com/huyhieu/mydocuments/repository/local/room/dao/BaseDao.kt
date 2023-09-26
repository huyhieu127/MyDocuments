package com.huyhieu.mydocuments.repository.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T)

    @Update
    fun update(vararg obj: T)

    @Delete
    fun delete(vararg obj: T)
}
