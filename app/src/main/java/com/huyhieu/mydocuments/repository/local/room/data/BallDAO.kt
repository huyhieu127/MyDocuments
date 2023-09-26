package com.huyhieu.mydocuments.repository.local.room.data

import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.huyhieu.mydocuments.repository.local.room.dao.BaseDao

interface BallDAO : BaseDao<Ball> {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override fun insert(vararg obj: Ball)

    @Query("SELECT name FROM Ball")
    fun getBall(): LiveData<Ball>
}

@Entity
class Ball(@PrimaryKey(autoGenerate = true) var id: Int, var name: String = "")