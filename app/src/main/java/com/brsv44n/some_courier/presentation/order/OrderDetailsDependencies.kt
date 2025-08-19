package com.brsv44n.some_courier.presentation.order

import com.brsv44n.some_courier.domain.repository.OrderRepository
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter
import com.brsv44n.some_courier.core.popup.MessageNotifier
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface OrderDetailsDependencies {
    val storeFactory: StoreFactory
    val dispatchers: CoroutineDispatchers
    val externalAppNavigator: ExternalAppNavigator
    val orderRepository: OrderRepository
    val errorHandler: ErrorHandler
    val dateTimeFormatter: DateTimeFormatter
    val profileRepository: ProfileRepository
    val messageNotifier: MessageNotifier
    val resourceManager: ResourceManager
}
