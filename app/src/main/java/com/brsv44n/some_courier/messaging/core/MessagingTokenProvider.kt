package com.brsv44n.some_courier.messaging.core

import com.brsv44n.some_courier.domain.models.PushToken

interface MessagingTokenProvider {

    suspend fun isServiceAvailable(): Boolean

    suspend fun getToken(): PushToken?
}
