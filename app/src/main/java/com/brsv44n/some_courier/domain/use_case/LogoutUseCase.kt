package com.brsv44n.some_courier.domain.use_case

import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.session.SessionManager
import me.tatarka.inject.annotations.Inject

@Inject
class LogoutUseCase(
    private val profileRepository: ProfileRepository,
    private val sessionManager: SessionManager,
) {

    suspend fun invoke(isTriggeredByUser: Boolean) {
        if (isTriggeredByUser) profileRepository.logout()
        sessionManager.clearSession()
    }
}
