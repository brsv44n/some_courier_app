package com.brsv44n.some_courier.di.dependencies

import com.brsv44n.some_courier.data.OrdersDataRepository
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.repository.RoutesRepository
import com.brsv44n.some_courier.presentation.in_process_orders.InProcessOrdersDependencies
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter
import com.brsv44n.some_courier.core.popup.MessageNotifier
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Inject

@Inject
class InProcessOrdersDependenciesImpl(
    override val dispatchers: CoroutineDispatchers,
    override val storeFactory: StoreFactory,
    override val routesRepository: RoutesRepository,
    override val profileRepository: ProfileRepository,
    override val ordersListRepository: OrdersDataRepository,
    override val externalAppNavigator: ExternalAppNavigator,
    override val messageNotifier: MessageNotifier,
    override val errorHandler: ErrorHandler,
    override val resourceManager: ResourceManager,
    override val dateTimeFormatter: DateTimeFormatter,
) : InProcessOrdersDependencies
