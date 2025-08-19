package com.brsv44n.some_courier.domain.repository

import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.core.domain.entities.Data
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUser(): Flow<Data<User>>

    suspend fun fetchUser(): Result<User>

    suspend fun logout(): Result<Unit>
}
