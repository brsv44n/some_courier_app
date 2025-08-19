package com.brsv44n.some_courier.messaging.rustore

import com.brsv44n.some_courier.domain.models.PushToken
import com.brsv44n.some_courier.domain.models.PushTokenProvider
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import me.tatarka.inject.annotations.Inject
import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult
import ru.rustore.sdk.core.tasks.Task
import ru.rustore.sdk.pushclient.RuStorePushClient
import kotlin.coroutines.resume

@Inject
class RustoreMessagingTokenProvider : MessagingTokenProvider {
    override suspend fun isServiceAvailable(): Boolean {
        val task: Task<FeatureAvailabilityResult> = RuStorePushClient.checkPushAvailability()
        val result: Result<FeatureAvailabilityResult> = task.wrapInCoroutine()

        return result.getOrNull() == FeatureAvailabilityResult.Available
    }

    override suspend fun getToken(): PushToken {
        val pushTokenTask = RuStorePushClient.getToken()
        return pushTokenTask.wrapInCoroutine().getOrThrow()
            .let { PushToken.Available(it, PushTokenProvider.RUSTORE) }
    }
}

suspend fun <T> Task<T>.wrapInCoroutine(): Result<T> = suspendCancellableCoroutine { cont ->

    cont.invokeOnCancellation { cancel() }

    addOnSuccessListener { value ->
        if (cont.isActive) {
            cont.resume(Result.success(value))
        }
    }
    addOnFailureListener { error ->
        if (cont.isActive) {
            cont.resume(Result.failure(error))
        }
    }
}
