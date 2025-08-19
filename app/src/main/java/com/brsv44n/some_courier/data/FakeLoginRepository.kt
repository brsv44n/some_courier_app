package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.domain.models.PushToken
import com.brsv44n.some_courier.domain.repository.LoginRepository
import com.brsv44n.some_courier.core.utils.AuthHolder
import kotlinx.coroutines.delay
import me.tatarka.inject.annotations.Inject

@Inject
class FakeLoginRepository(
    private val authHolder: AuthHolder,
) : LoginRepository {

    override suspend fun login(
        login: String,
        pushToken: PushToken?
    ): Result<Unit> {
        delay(2000L)
        return Result.success("token")
            .onSuccess { authHolder.setTokens(tokens = AuthHolder.Tokens(null, it)) }.map { }
    }

    override suspend fun logout() = Result.success(Unit)

}
