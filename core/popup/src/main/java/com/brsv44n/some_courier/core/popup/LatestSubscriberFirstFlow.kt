package com.brsv44n.some_courier.core.popup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class LatestSubscriberFirstFlow<T>(private val replay: Int = 0) {

    private val collectors = mutableListOf<MutableSharedFlow<T>>()

    fun subscribe(): Flow<T> {
        val stateFlow = MutableSharedFlow<T>(replay = replay)

        synchronized(collectors) {
            collectors.add(0, stateFlow)
        }

        return stateFlow
    }

    suspend fun emit(value: T) {
        collectors.firstOrNull()?.emit(value)
    }

    @Suppress("IgnoredReturnValue")
    fun unsubscribe(flow: Flow<T>) {
        synchronized(collectors) {
            val index = collectors.indexOfFirst { it == flow }
            if (index != -1) {
                collectors.removeAt(index)
            }
        }
    }
}
