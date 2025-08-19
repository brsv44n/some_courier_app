package com.brsv44n.some_courier.domain.repository

import com.brsv44n.some_courier.domain.models.PushToken

interface LoginRepository {

    suspend fun login(
        login: String,
        pushToken: PushToken? = PushToken.Empty
    ): Result<Unit>

    suspend fun logout(): Result<Unit>
}
