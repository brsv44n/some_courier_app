package com.brsv44n.some_courier.domain.repository

import com.brsv44n.some_courier.domain.models.PushToken
import com.brsv44n.some_courier.domain.models.RemoteSettings

interface SettingsRepository {
    suspend fun getSettings(): Result<RemoteSettings>

    suspend fun updatePushToken(
        pushToken: PushToken = PushToken.Empty
    ): Result<Unit>
}
