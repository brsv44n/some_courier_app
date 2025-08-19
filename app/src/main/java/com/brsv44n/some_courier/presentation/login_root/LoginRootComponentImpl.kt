package com.brsv44n.some_courier.presentation.login_root

import com.brsv44n.some_courier.di.components.LoginDiComponent
import com.brsv44n.some_courier.di.components.create
import com.brsv44n.some_courier.presentation.login_root.LoginRootComponent.Output
import com.brsv44n.some_courier.presentation.login_root.login.LoginComponent
import com.brsv44n.some_courier.presentation.login_root.login_support.LoginSupportComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.backStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

internal class LoginRootComponentImpl(
    componentContext: ComponentContext,
    private val dependencies: LoginRootDependencies,
    private val output: (Output) -> Unit,
) : LoginRootComponent, ComponentContext by componentContext {

    override val childStack: Value<ChildStack<*, LoginRootComponent.Child>>
        get() = stack

    private val navigation = StackNavigation<Config>()

    private val diComponent by lazy {
        instanceKeeper.getOrCreate {
            LoginDiComponent::class.create(dependencies)
        }
    }

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Login,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override fun onBackClicked() {
        if (stack.backStack.isEmpty()) {
            output.invoke(Output.Exit)
        } else {
            navigation.pop()
        }
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): LoginRootComponent.Child {
        return when (config) {
            is Config.Login -> {
                LoginRootComponent.Child.Login(
                    diComponent.loginComponentFactory.invoke(
                        componentContext = componentContext,
                        output = ::onLoginOutput
                    )
                )
            }

            Config.LoginSupport -> {
                LoginRootComponent.Child.Support(
                    diComponent.loginSupportComponentFactory.invoke(
                        componentContext = componentContext,
                        output = ::onLoginSupportOutput,
                    )
                )
            }
        }
    }

    private fun onLoginOutput(output: LoginComponent.Output) {
        when (output) {
            is LoginComponent.Output.LoginSupport -> navigation.push(Config.LoginSupport)
            LoginComponent.Output.Exit -> navigation.pop()
            LoginComponent.Output.LoginSucceed -> this.output.invoke(Output.LoginSucceed)
        }
    }

    private fun onLoginSupportOutput(output: LoginSupportComponent.Output) {
        when (output) {
            is LoginSupportComponent.Output.Exit -> {
                navigation.pop()
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object LoginSupport : Config

        @Serializable
        data object Login : Config
    }
}

@Inject
class LoginRootComponentFactory(
    private val dependencies: LoginRootDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
        output: (Output) -> Unit,
    ): LoginRootComponent {
        return LoginRootComponentImpl(
            componentContext = componentContext,
            output = output,
            dependencies = dependencies,
        )
    }
}
