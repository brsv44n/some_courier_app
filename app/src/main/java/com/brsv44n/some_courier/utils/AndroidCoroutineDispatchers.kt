package com.brsv44n.some_courier.utils

import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
@Suppress("InjectDispatcher")
class AndroidCoroutineDispatchers : CoroutineDispatchers {

    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}
