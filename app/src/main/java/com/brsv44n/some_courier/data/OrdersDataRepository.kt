package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.data.remote.dto.toDomain
import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.domain.models.OrdersListType
import com.brsv44n.some_courier.domain.repository.OrderRepository
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.core.data.remote.toPaginatedList
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.domain.entities.PagedList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class OrdersDataRepository(
    private val apiService: ApiService,
) : OrderRepository {

    override val ordersUpdatesFlow: MutableSharedFlow<Order>
        get() = _ordersUpdatesFlow

    private val _ordersUpdatesFlow = MutableSharedFlow<Order>()

    override suspend fun getOrders(
        type: OrdersListType,
        page: Int,
    ): Result<PagedList<Order>> = runCatching {
        apiService.getOrdersHistory(type = type.name.lowercase(), page = page)
            .toPaginatedList { it.toDomain }
    }

    override suspend fun getOrder(orderId: Long): Result<Order> = runCatching {
        apiService.getOrder(orderId).order.toDomain
    }

    override suspend fun changeOrderStatus(
        orderId: Long,
        newOrderStatus: OrderStatus,
    ): Result<Order> = runCatching {

        apiService.changeOrderStatus(
            body = buildJsonObject {
                put("orderId", orderId)
                put("newStatus", newOrderStatus.name.lowercase())
            }
        ).toDomain.also { _ordersUpdatesFlow.emit(it) }
    }

    override suspend fun markOrderAsWithProblem(orderId: Long): Result<Order> = runCatching {
        apiService.markOrderAsWithProblem(
            body = buildJsonObject {
                put("orderId", orderId)
            }
        ).toDomain.also { _ordersUpdatesFlow.emit(it) }
    }

}
