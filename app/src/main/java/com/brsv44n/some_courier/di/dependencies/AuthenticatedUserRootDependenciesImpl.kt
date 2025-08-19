package com.brsv44n.some_courier.di.dependencies

import com.brsv44n.some_courier.data.PermissionsObserver
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import com.brsv44n.some_courier.presentation.authenticated_user_root.AuthenticatedUserRootDependencies
import com.brsv44n.some_courier.presentation.drawer.DrawerComponentFactory
import com.brsv44n.some_courier.presentation.in_process_orders.InProcessOrdersComponentFactory
import com.brsv44n.some_courier.presentation.order.OrderDetailsComponentFactory
import com.brsv44n.some_courier.presentation.orders_history.OrdersHistoryComponentFactory
import com.brsv44n.some_courier.presentation.permissions.PermissionsComponentFactory
import com.brsv44n.some_courier.presentation.profile.ProfileComponentFactory
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Inject

@Inject
class AuthenticatedUserRootDependenciesImpl(
    override val profileComponentFactory: ProfileComponentFactory,
    override val drawerComponentFactory: DrawerComponentFactory,
    override val orderDetailsComponentFactory: OrderDetailsComponentFactory,
    override val ordersHistoryComponentFactory: OrdersHistoryComponentFactory,
    override val inProcessOrdersComponentFactory: InProcessOrdersComponentFactory,
    override val dispatchers: CoroutineDispatchers,
    override val storeFactory: StoreFactory,
    override val resourceManager: ResourceManager,
    override val permissionsComponentFactory: PermissionsComponentFactory,
    override val permissionsObserver: PermissionsObserver,
    override val settingsRepository: SettingsRepository,
    override val profileRepository: ProfileRepository,
    override val messagingTokenProvider: MessagingTokenProvider,
    override val externalAppNavigator: ExternalAppNavigator,
) : AuthenticatedUserRootDependencies
