package com.brsv44n.some_courier.domain.use_case

import com.brsv44n.some_courier.domain.error.LoginValidationError
import com.brsv44n.some_courier.domain.repository.LoginRepository
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import me.tatarka.inject.annotations.Inject

@Inject
class LoginUseCase(
    private val loginRepository: LoginRepository,
    private val messagingTokenProvider: MessagingTokenProvider
) {
    suspend operator fun invoke(
        login: String,
    ): Result<Unit> {
        val isLoginValid = login.isValidPhone()
        if (!isLoginValid) {
            return Result.failure(LoginValidationError(isLoginValid))
        }
        return loginRepository.login(
            login = login.rawPhone(),
            pushToken = messagingTokenProvider.getToken()
        )
    }

    private fun String.isValidPhone(): Boolean {
        val rawPhone = rawPhone()
        return rawPhone.matches(Regex("^\\+7\\d{10}$"))
    }

    private fun String.rawPhone(): String {
        val rawString = replace(Regex("[\\s()\\-]|^\\+"), "")
        return if (rawString.length == 10) {
            "+7$rawString"
        } else if (rawString.length == 11 && (rawString.startsWith("8") || rawString.startsWith("7"))) {
            "+7${rawString.substring(1)}"
        } else {
            rawString
        }
    }
}
