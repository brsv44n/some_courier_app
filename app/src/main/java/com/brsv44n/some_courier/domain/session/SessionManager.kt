package com.brsv44n.some_courier.domain.session

import com.brsv44n.some_courier.core.di.annotations.Singleton
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class SessionManager(
    private val clearables: Set<SessionClearable>
) {

    suspend fun clearSession() {
        clearables.forEach { it.clearSessionInfo() }
    }
}
