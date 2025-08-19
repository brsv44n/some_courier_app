package com.brsv44n.some_courier.presentation.root.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.brsv44n.some_courier.presentation.authenticated_user_root.ui.AuthenticatedUserRootContent
import com.brsv44n.some_courier.presentation.login_root.ui.LoginRootContent
import com.brsv44n.some_courier.presentation.order.ui.OrderScreenContent
import com.brsv44n.some_courier.presentation.root.RootComponent
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun RootContent(
    modifier: Modifier = Modifier,
    component: RootComponent,
) {
    val stack by component.childStack.subscribeAsState()

    Children(
        modifier = modifier,
        stack = stack,
    ) { child ->
        when (val activeChild = child.instance) {
            is RootComponent.Child.LoginRoot -> {
                LoginRootContent(
                    modifier = Modifier.fillMaxSize(),
                    component = activeChild.component
                )
            }

            is RootComponent.Child.AuthenticatedUserRoot -> {
                AuthenticatedUserRootContent(
                    modifier = Modifier.fillMaxSize(),
                    component = activeChild.component
                )
            }

            is RootComponent.Child.Order -> {
                OrderScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    component = activeChild.component,
                )
            }
        }
    }
}
