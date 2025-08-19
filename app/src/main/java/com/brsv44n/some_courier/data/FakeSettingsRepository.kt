package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.domain.models.PushToken
import com.brsv44n.some_courier.domain.models.RemoteSettings
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import me.tatarka.inject.annotations.Inject

@Inject
class FakeSettingsRepository : SettingsRepository {
    override suspend fun getSettings(): Result<RemoteSettings> =
        Result.success(RemoteSettings(managerPhone = "8999999999"))

    override suspend fun updatePushToken(pushToken: PushToken): Result<Unit> = Result.success(Unit)
}
