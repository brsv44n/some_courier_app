package com.brsv44n.some_courier.presentation.authenticated_user_root

import com.brsv44n.some_courier.presentation.drawer.DrawerComponent
import com.brsv44n.some_courier.presentation.in_process_orders.InProcessOrdersComponent
import com.brsv44n.some_courier.presentation.orders_history.OrdersHistoryComponent
import com.brsv44n.some_courier.presentation.permissions.PermissionsComponent
import com.brsv44n.some_courier.presentation.profile.ProfileComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface AuthenticatedUserRootComponent {

    val navbarTitle: Value<String>

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    val isDrawerOpened: Value<Boolean>

    val childStack: Value<ChildStack<*, Child>>
    val drawerComponent: DrawerComponent

    fun drawerToggled(isOpened: Boolean)

    fun callManagerClicked()

    sealed interface Output {
        data object OpenLogin : Output
        data class OpenOrderDetails(val orderId: Long) : Output
    }

    sealed interface Child {
        data class Profile(val component: ProfileComponent) : Child
        data class OrdersHistory(val component: OrdersHistoryComponent) : Child
        data class InProcessOrders(val component: InProcessOrdersComponent) : Child
        data class Settings(val component: PermissionsComponent) : Child
        data class Permissions(val component: PermissionsComponent) : Child
    }
}
