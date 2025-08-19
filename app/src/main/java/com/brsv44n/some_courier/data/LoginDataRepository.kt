package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.domain.models.PushToken
import com.brsv44n.some_courier.domain.repository.LoginRepository
import com.brsv44n.some_courier.core.utils.AuthHolder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import me.tatarka.inject.annotations.Inject

@Inject
class LoginDataRepository(
    private val api: ApiService,
    private val authHolder: AuthHolder,
) : LoginRepository {

    override suspend fun login(
        login: String,
        pushToken: PushToken?,
    ): Result<Unit> {
        return runCatching {
            val response = api.login(
                body = buildJsonObject {
                    put("phone", login)
                    if (pushToken is PushToken.Available) {
                        put("token", pushToken.token)
                        put("provider", pushToken.provider.serviceName)
                    }
                }
            )
            authHolder.setTokens(
                AuthHolder.Tokens(refreshToken = null, accessToken = response.accessToken)
            )
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching { api.logout() }
    }

}
