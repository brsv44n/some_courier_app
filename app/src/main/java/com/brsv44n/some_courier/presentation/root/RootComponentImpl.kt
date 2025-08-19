package com.brsv44n.some_courier.presentation.root

import com.brsv44n.some_courier.presentation.authenticated_user_root.AuthenticatedUserRootComponent
import com.brsv44n.some_courier.presentation.login_root.LoginRootComponent
import com.brsv44n.some_courier.presentation.order.OrderComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

class RootComponentImpl(
    componentContext: ComponentContext,
    private val dependencies: RootComponentDependencies,
    orderId: Long?,
) : RootComponent, ComponentContext by componentContext {

    override val childStack: Value<ChildStack<*, RootComponent.Child>>
        get() = stack

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = {
            if (orderId != null && orderId != -1L && dependencies.authHolder.isLoggedIn) {
                listOf(Config.AuthenticatedUserRoot, Config.Order(orderId))
            } else if (dependencies.authHolder.isLoggedIn) {
                listOf(Config.AuthenticatedUserRoot)
            } else {
                listOf(Config.LoginRoot)
            }
        },
        handleBackButton = true,
        childFactory = ::createChild
    )

    private val scope = coroutineScope(dependencies.dispatchers.main + SupervisorJob())

    init {
        dependencies.locationServiceLauncherComponentFactory.invoke(
            componentContext = componentContext.childContext("location"),
        )

        dependencies.unauthorizedErrorEmitter.events.onEach {
            dependencies.logoutUseCase.invoke(false)
            if (stack.active.configuration != Config.LoginRoot) {
                navigation.replaceAll(Config.LoginRoot)
            }
        }.launchIn(scope)
    }

    override fun onNewIntent(orderId: Long) {
        navigation.pushNew(Config.Order(orderId))
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child {
        return when (config) {
            is Config.LoginRoot -> {
                RootComponent.Child.LoginRoot(
                    dependencies.loginFactory.invoke(
                        componentContext = componentContext,
                        output = ::onLoginOutput
                    )
                )
            }

            is Config.AuthenticatedUserRoot -> {
                RootComponent.Child.AuthenticatedUserRoot(
                    dependencies.authenticatedUserRootComponentFactory.invoke(
                        componentContext = componentContext,
                        output = ::onAuthenticatedUserOutput
                    )
                )
            }

            is Config.Order -> {
                RootComponent.Child.Order(
                    dependencies.orderDetailsComponentFactory.invoke(
                        componentContext = componentContext,
                        output = ::onOrderOutput,
                        orderId = config.orderId,
                    )
                )
            }
        }
    }

    private fun onLoginOutput(output: LoginRootComponent.Output) {
        when (output) {
            LoginRootComponent.Output.Exit -> navigation.pop()
            LoginRootComponent.Output.LoginSucceed -> navigation.replaceAll(
                Config.AuthenticatedUserRoot
            )
        }
    }

    private fun onAuthenticatedUserOutput(output: AuthenticatedUserRootComponent.Output) {
        when (output) {
            AuthenticatedUserRootComponent.Output.OpenLogin -> {
                navigation.replaceAll(
                    Config.LoginRoot
                )
            }

            is AuthenticatedUserRootComponent.Output.OpenOrderDetails -> {
                navigation.pushToFront(Config.Order(output.orderId))
            }
        }
    }

    private fun onOrderOutput(output: OrderComponent.Output) {
        when (output) {
            OrderComponent.Output.Exit -> {
                navigation.pop()
            }
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object LoginRoot : Config

        @Serializable
        data object AuthenticatedUserRoot : Config

        @Serializable
        data class Order(val orderId: Long) : Config
    }
}

@Inject
class RootComponentFactory(
    private val dependencies: RootComponentDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
        orderId: Long?,
    ): RootComponent =
        RootComponentImpl(
            componentContext = componentContext,
            dependencies = dependencies,
            orderId = orderId
        )
}
