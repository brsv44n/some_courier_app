package com.brsv44n.some_courier.presentation.in_process_orders

import com.brsv44n.some_courier.data.OrdersDataRepository
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.repository.RoutesRepository
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter
import com.brsv44n.some_courier.core.popup.MessageNotifier
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface InProcessOrdersDependencies {
    val storeFactory: StoreFactory
    val dispatchers: CoroutineDispatchers
    val routesRepository: RoutesRepository
    val profileRepository: ProfileRepository
    val ordersListRepository: OrdersDataRepository
    val externalAppNavigator: ExternalAppNavigator
    val messageNotifier: MessageNotifier
    val errorHandler: ErrorHandler
    val resourceManager: ResourceManager
    val dateTimeFormatter: DateTimeFormatter
}
