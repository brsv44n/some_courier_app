package com.brsv44n.some_courier.di.dependencies

import com.brsv44n.some_courier.domain.repository.OrderRepository
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.presentation.order.OrderDetailsDependencies
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter
import com.brsv44n.some_courier.core.popup.MessageNotifier
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Inject

@Inject
class OrderDetailsDependenciesImpl(
    override val storeFactory: StoreFactory,
    override val dispatchers: CoroutineDispatchers,
    override val externalAppNavigator: ExternalAppNavigator,
    override val orderRepository: OrderRepository,
    override val errorHandler: ErrorHandler,
    override val dateTimeFormatter: DateTimeFormatter,
    override val profileRepository: ProfileRepository,
    override val messageNotifier: MessageNotifier,
    override val resourceManager: ResourceManager
) : OrderDetailsDependencies
