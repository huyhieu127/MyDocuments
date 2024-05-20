package com.huyhieu.contentprovider.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huyhieu.contentprovider.room.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Long): Int

    @Query("UPDATE users SET name = :name, age = :age WHERE id = :id")
    suspend fun updateUser(id: Long, name: String, age: Int): Int
    /**
     * For Content Provider
     * */
    @Query("SELECT * FROM users")
    fun getAllUsersCP(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserByIdCP(id: Long): User?

}