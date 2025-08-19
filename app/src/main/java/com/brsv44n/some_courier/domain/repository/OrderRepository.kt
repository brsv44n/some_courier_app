package com.brsv44n.some_courier.domain.repository

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.domain.models.OrdersListType
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.core.domain.entities.PagedList
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    val ordersUpdatesFlow: Flow<Order>

    suspend fun getOrders(
        type: OrdersListType,
        page: Int,
    ): Result<PagedList<Order>>

    suspend fun getOrder(orderId: Long): Result<Order>

    suspend fun changeOrderStatus(
        orderId: Long,
        newOrderStatus: OrderStatus,
    ): Result<Order>

    suspend fun markOrderAsWithProblem(
        orderId: Long,
    ): Result<Order>
}
