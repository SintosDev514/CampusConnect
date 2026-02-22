package com.campusconnectplus.data.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUsers(): Flow<List<User>>
    suspend fun upsert(user: User)
    suspend fun delete(id: Long)

    // optional but matches your AdminUsersScreen errors (setRole / toggleActive)
    suspend fun setRole(userId: Long, role: UserRole)
    suspend fun setActive(userId: Long, active: Boolean)
}
