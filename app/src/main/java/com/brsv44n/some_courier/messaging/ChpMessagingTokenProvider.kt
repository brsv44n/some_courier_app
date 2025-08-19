package com.brsv44n.some_courier.messaging

import com.brsv44n.some_courier.domain.models.PushToken
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import com.brsv44n.some_courier.messaging.rustore.RustoreMessagingTokenProvider
import me.tatarka.inject.annotations.Inject
import timber.log.Timber

@Inject
class ChpMessagingTokenProvider(
    private val rustoreMessagingTokenProvider: RustoreMessagingTokenProvider
) : MessagingTokenProvider {

    override suspend fun isServiceAvailable(): Boolean {
        val isRustoreAvailable = rustoreMessagingTokenProvider.isServiceAvailable()
        return isRustoreAvailable
    }

    override suspend fun getToken(): PushToken? {
        return if (rustoreMessagingTokenProvider.isServiceAvailable()) {
            getTokenFromRustore()
        } else {
            null
        }
    }

    private suspend fun getTokenFromRustore(): PushToken? =
        runCatching {
            rustoreMessagingTokenProvider.getToken()
        }.getOrElse {
            Timber.tag("ChpRustoreMessagingTokenProvider").e(it)
            null
        }
}
