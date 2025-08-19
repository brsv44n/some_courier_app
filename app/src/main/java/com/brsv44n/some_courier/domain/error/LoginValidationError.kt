package com.brsv44n.some_courier.domain.error

class LoginValidationError(
    val isLoginValid: Boolean,
) : Throwable()
