package com.brsv44n.some_courier.presentation.authenticated_user_root

import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.authenticated_user_root.store.AuthenticatedUserStoreFactory
import com.brsv44n.some_courier.presentation.drawer.DrawerComponent
import com.brsv44n.some_courier.presentation.in_process_orders.InProcessOrdersComponent
import com.brsv44n.some_courier.presentation.orders_history.OrdersHistoryComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.coroutines.repeatOnLifecycle
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

internal class AuthenticatedUserRootComponentImpl(
    componentContext: ComponentContext,
    private val dependencies: AuthenticatedUserRootDependencies,
    private val output: (AuthenticatedUserRootComponent.Output) -> Unit,
) : AuthenticatedUserRootComponent, ComponentContext by componentContext {

    override val navbarTitle: Value<String>
        get() = childStack.map { children ->
            return@map when (children.active.configuration) {
                is Config.Profile -> dependencies.resourceManager.getString(R.string.title_profile_navbar)
                is Config.OrdersHistory -> dependencies.resourceManager.getString(R.string.title_orders_history_navbar)
                is Config.MainRoot -> dependencies.resourceManager.getString(R.string.label_drawer_orders)
                is Config.Settings -> dependencies.resourceManager.getString(R.string.title_settings_navbar)
                else -> ""
            }
        }

    override val childStack: Value<ChildStack<*, AuthenticatedUserRootComponent.Child>>
        get() = stack

    override val drawerComponent: DrawerComponent = dependencies.drawerComponentFactory.invoke(
        componentContext = componentContext,
        output = ::onDrawerOutput
    )

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    override val isDrawerOpened: MutableValue<Boolean> = MutableValue(false)

    @Suppress("UnusedPrivateProperty")
    private val store = instanceKeeper.getStore {
        AuthenticatedUserStoreFactory(
            dispatchers = dependencies.dispatchers,
            storeFactory = dependencies.storeFactory,
            settingsRepository = dependencies.settingsRepository,
            profileRepository = dependencies.profileRepository,
            messagingTokenProvider = dependencies.messagingTokenProvider
        ).provide()
    }

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = getInitialConfiguration(),
        handleBackButton = true,
        childFactory = ::createChild
    )

    private val scope = coroutineScope(dependencies.dispatchers.main + SupervisorJob())

    init {
        scope.launch {
            dependencies.permissionsObserver.state.map { it.areAllPermissionsGranted }
                .distinctUntilChanged()
                .drop(1)
                .onEach { areAllPermissionsGranted ->
                    if (areAllPermissionsGranted) {
                        navigation.navigate {
                            val resultStack = it.filter { config -> config !is Config.Permissions }
                            resultStack.ifEmpty { listOf(Config.MainRoot) }
                        }
                    } else {
                        navigation.navigate { stack ->
                            val resultStack = stack.filterIsInstance<Config.Settings>()
                            if (resultStack.isNotEmpty() && stack.last() !is Config.Settings) {
                                resultStack + Config.Permissions
                            } else {
                                resultStack.ifEmpty { listOf(Config.Permissions) }
                            }
                        }
                    }
                }.launchIn(this)
        }

        scope.launch {
            lifecycle.repeatOnLifecycle(
                minActiveState = Lifecycle.State.RESUMED
            ) {
                dependencies.permissionsObserver.invalidate()
            }
        }
    }

    override fun drawerToggled(isOpened: Boolean) {
        isDrawerOpened.value = isOpened
    }

    override fun callManagerClicked() {
        store.state.user?.let {
            dependencies.externalAppNavigator.openDialApp(phone = it.restaurant.managerPhone)
        }
    }

    private fun getInitialConfiguration(): Config {
        return if (dependencies.permissionsObserver.state.value.areAllPermissionsGranted) {
            Config.MainRoot
        } else {
            Config.Permissions
        }
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): AuthenticatedUserRootComponent.Child =
        when (config) {

            Config.MainRoot -> {
                AuthenticatedUserRootComponent.Child.InProcessOrders(
                    dependencies.inProcessOrdersComponentFactory.invoke(
                        componentContext = componentContext,
                        output = ::onInProcessOrdersOutput
                    )
                )
            }

            Config.Profile -> {
                AuthenticatedUserRootComponent.Child.Profile(
                    dependencies.profileComponentFactory.invoke(
                        componentContext
                    )
                )
            }

            Config.OrdersHistory -> {
                AuthenticatedUserRootComponent.Child.OrdersHistory(
                    dependencies.ordersHistoryComponentFactory.invoke(
                        componentContext = componentContext,
                        output = ::onOrdersHistoryOutput
                    )
                )
            }

            Config.Settings -> {
                AuthenticatedUserRootComponent.Child.Settings(
                    dependencies.permissionsComponentFactory.invoke(componentContext)
                )
            }

            Config.Permissions -> {
                AuthenticatedUserRootComponent.Child.Permissions(
                    dependencies.permissionsComponentFactory.invoke(componentContext)
                )
            }
        }

    private fun onDrawerOutput(output: DrawerComponent.Output) {
        when (output) {
            DrawerComponent.Output.OpenLogin -> {
                this.output.invoke(AuthenticatedUserRootComponent.Output.OpenLogin)
            }

            DrawerComponent.Output.HistoryClicked -> {
                isDrawerOpened.value = false
                navigation.pushToFront(getTargetConfig(Config.OrdersHistory))
            }

            DrawerComponent.Output.ProfileClicked -> {
                isDrawerOpened.value = false
                navigation.pushToFront(getTargetConfig(Config.Profile))
            }

            DrawerComponent.Output.OrdersClicked -> {
                isDrawerOpened.value = false
                navigation.pushToFront(getTargetConfig(Config.MainRoot))
            }

            DrawerComponent.Output.SettingsClicked -> {
                isDrawerOpened.value = false
                navigation.pushToFront(Config.Settings)
            }
        }
    }

    private fun onOrdersHistoryOutput(output: OrdersHistoryComponent.Output) {
        when (output) {
            OrdersHistoryComponent.Output.Exit -> {
                navigation.pop()
            }

            is OrdersHistoryComponent.Output.OrderClicked -> {
                output(AuthenticatedUserRootComponent.Output.OpenOrderDetails(output.orderId))
            }
        }
    }

    private fun onInProcessOrdersOutput(output: InProcessOrdersComponent.Output) {
        when (output) {
            is InProcessOrdersComponent.Output.OrderClicked -> {
                this.output(AuthenticatedUserRootComponent.Output.OpenOrderDetails(output.orderId))
            }

            else -> {}
        }
    }

    private fun getTargetConfig(config: Config): Config {
        return if (dependencies.permissionsObserver.state.value.areAllPermissionsGranted) {
            config
        } else {
            Config.Permissions
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Profile : Config

        @Serializable
        data object OrdersHistory : Config

        @Serializable
        data object MainRoot : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data object Permissions : Config
    }
}

@Inject
class AuthenticatedUserRootComponentFactory(
    private val dependencies: AuthenticatedUserRootDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
        output: (AuthenticatedUserRootComponent.Output) -> Unit,
    ): AuthenticatedUserRootComponent {
        return AuthenticatedUserRootComponentImpl(
            componentContext = componentContext,
            dependencies = dependencies,
            output = output
        )
    }
}
