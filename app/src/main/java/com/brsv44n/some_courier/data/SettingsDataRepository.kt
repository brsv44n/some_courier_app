package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.data.remote.dto.toDomain
import com.brsv44n.some_courier.domain.models.PushToken
import com.brsv44n.some_courier.domain.models.RemoteSettings
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import com.brsv44n.some_courier.core.data.caching.CachingSource
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import me.tatarka.inject.annotations.Inject

@Inject
class SettingsDataRepository(
    private val api: ApiService,
) : SettingsRepository {

    companion object {
        private const val SETTINGS_KEY = "settings_key"
    }

    private val settingsCacheSource = CachingSource<String, RemoteSettings> {
        api.settings().toDomain
    }

    override suspend fun getSettings(): Result<RemoteSettings> = runCatching {
        settingsCacheSource.get(SETTINGS_KEY)
    }

    override suspend fun updatePushToken(
        pushToken: PushToken
    ): Result<Unit> {
        return runCatching {
            api.updatePushToken(
                body = buildJsonObject {
                    if (pushToken is PushToken.Available) {
                        put("token", pushToken.token)
                        put("provider", pushToken.provider.serviceName)
                    }
                }
            )
        }
    }

}
