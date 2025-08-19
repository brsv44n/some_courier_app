package com.brsv44n.some_courier.core.utils

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
