package com.brsv44n.some_courier.presentation.orders_history

import com.brsv44n.some_courier.domain.repository.OrderRepository
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface OrdersHistoryDependencies {
    val storeFactory: StoreFactory
    val dispatchers: CoroutineDispatchers
    val ordersRepository: OrderRepository
    val errorHandler: ErrorHandler
    val dateTimeFormatter: DateTimeFormatter
}
