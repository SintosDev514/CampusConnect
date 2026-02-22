package com.campusconnectplus.data.local.dao

import androidx.room.*
import com.campusconnectplus.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<UserEntity>>

    @Upsert
    suspend fun upsert(entity: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE users SET role = :role WHERE id = :id")
    suspend fun updateRole(id: String, role: String)

    @Query("UPDATE users SET active = :active WHERE id = :id")
    suspend fun updateActive(id: String, active: Boolean)
}
