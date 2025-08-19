package com.brsv44n.some_courier.presentation.root

import com.brsv44n.some_courier.presentation.authenticated_user_root.AuthenticatedUserRootComponent
import com.brsv44n.some_courier.presentation.login_root.LoginRootComponent
import com.brsv44n.some_courier.presentation.order.OrderComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class LoginRoot(val component: LoginRootComponent) : Child
        data class AuthenticatedUserRoot(val component: AuthenticatedUserRootComponent) : Child
        data class Order(val component: OrderComponent) : Child
    }

    fun onNewIntent(orderId: Long)
}
