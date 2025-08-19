package com.brsv44n.some_courier.di.dependencies

import com.brsv44n.some_courier.domain.repository.OrderRepository
import com.brsv44n.some_courier.presentation.orders_history.OrdersHistoryDependencies
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Inject

@Inject
class OrdersHistoryDependenciesImpl(
    override val dispatchers: CoroutineDispatchers,
    override val storeFactory: StoreFactory,
    override val ordersRepository: OrderRepository,
    override val errorHandler: ErrorHandler,
    override val dateTimeFormatter: DateTimeFormatter,
) : OrdersHistoryDependencies
