package com.brsv44n.some_courier.presentation.authenticated_user_root

import com.brsv44n.some_courier.data.PermissionsObserver
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
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

@Suppress("ComplexInterface")
interface AuthenticatedUserRootDependencies {
    val profileComponentFactory: ProfileComponentFactory
    val drawerComponentFactory: DrawerComponentFactory
    val orderDetailsComponentFactory: OrderDetailsComponentFactory
    val ordersHistoryComponentFactory: OrdersHistoryComponentFactory
    val inProcessOrdersComponentFactory: InProcessOrdersComponentFactory
    val permissionsComponentFactory: PermissionsComponentFactory
    val storeFactory: StoreFactory
    val dispatchers: CoroutineDispatchers
    val resourceManager: ResourceManager
    val permissionsObserver: PermissionsObserver
    val settingsRepository: SettingsRepository
    val profileRepository: ProfileRepository
    val messagingTokenProvider: MessagingTokenProvider
    val externalAppNavigator: ExternalAppNavigator
}
