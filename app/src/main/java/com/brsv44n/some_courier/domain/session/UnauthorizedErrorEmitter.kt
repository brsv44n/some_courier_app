package com.brsv44n.some_courier.domain.session

import com.brsv44n.some_courier.core.di.annotations.Singleton
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class UnauthorizedErrorEmitter {

    val events: Flow<Unit>
        get() = _events

    private val _events = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    fun emitUnauthorizedError() {
        _events.tryEmit(Unit)
    }
}
