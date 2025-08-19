package com.brsv44n.some_courier.domain.models

sealed class PushToken {

    data object Empty : PushToken()

    data class Available(
        val token: String,
        val provider: PushTokenProvider,
    ) : PushToken()
}
