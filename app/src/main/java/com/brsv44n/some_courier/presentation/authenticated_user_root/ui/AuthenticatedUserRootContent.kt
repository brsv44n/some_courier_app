package com.brsv44n.some_courier.presentation.authenticated_user_root.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.authenticated_user_root.AuthenticatedUserRootComponent
import com.brsv44n.some_courier.presentation.drawer.ui.DrawerContent
import com.brsv44n.some_courier.presentation.in_process_orders.ui.InProcessOrdersContent
import com.brsv44n.some_courier.presentation.orders_history.ui.OrdersHistoryContent
import com.brsv44n.some_courier.presentation.permissions.ui.PermissionsContent
import com.brsv44n.some_courier.presentation.profile.ui.ProfileContent
import com.brsv44n.some_courier.presentation.settings.ui.SettingsContent
import com.brsv44n.some_courier.presentation.ui_kit.NavigationToolbar
import com.brsv44n.some_courier.presentation.ui_kit.OverlayProgress
import com.brsv44n.some_courier.theme.Typography
import com.brsv44n.some_courier.theme.bgPrimary
import com.brsv44n.some_courier.theme.icon
import com.brsv44n.some_courier.theme.onSurface
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch

@Composable
internal fun AuthenticatedUserRootContent(
    modifier: Modifier = Modifier,
    component: AuthenticatedUserRootComponent,
) {

    val isDrawerOpened by component.isDrawerOpened.subscribeAsState()
    val isProgressVisible by component.drawerComponent.isProgressVisible.subscribeAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(isDrawerOpened) {
        if (isDrawerOpened) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    LaunchedEffect(drawerState) {
        snapshotFlow { drawerState.currentValue }
            .collect {
                component.drawerToggled(drawerState.isOpen)
            }
    }

    val scope = rememberCoroutineScope()
    if (isProgressVisible) {
        OverlayProgress(modifier = Modifier.fillMaxSize())
    }
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.68f),
                drawerComponent = component.drawerComponent
            )
        }
    ) {

        val stack by component.childStack.subscribeAsState()
        val navbarTitle by component.navbarTitle.subscribeAsState()

        Scaffold(
            topBar = {
                NavigationToolbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 3.dp),
                    title = {
                        Text(
                            text = navbarTitle,
                            style = Typography.titleLarge,
                            color = onSurface
                        )
                    },
                    backgroundColor = bgPrimary,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_burger),
                                contentDescription = null,
                                tint = onSurface
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { component.callManagerClicked() }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_support_agent),
                                contentDescription = null,
                                tint = icon
                            )
                        }
                    }
                )
            }
        ) { paddings ->
            Children(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddings.calculateTopPadding()
                    ),
                stack = stack
            ) { child ->
                when (val activeChild = child.instance) {
                    is AuthenticatedUserRootComponent.Child.InProcessOrders -> {
                        InProcessOrdersContent(
                            modifier = Modifier.fillMaxSize(),
                            component = activeChild.component
                        )
                    }

                    is AuthenticatedUserRootComponent.Child.Profile -> {
                        ProfileContent(
                            modifier = Modifier.fillMaxSize(),
                            component = activeChild.component
                        )
                    }

                    is AuthenticatedUserRootComponent.Child.OrdersHistory -> {
                        OrdersHistoryContent(
                            modifier = Modifier.fillMaxSize(),
                            component = activeChild.component
                        )
                    }

                    is AuthenticatedUserRootComponent.Child.Settings -> {
                        SettingsContent(
                            modifier = Modifier.fillMaxSize(),
                            component = activeChild.component
                        )
                    }

                    is AuthenticatedUserRootComponent.Child.Permissions -> {
                        PermissionsContent(
                            modifier = Modifier.fillMaxSize(),
                            component = activeChild.component
                        )
                    }
                }
            }
        }
    }
}
