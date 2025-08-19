package com.brsv44n.some_courier.presentation.login_root.login_support

import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.login_root.LoginRootDependencies
import com.brsv44n.some_courier.presentation.login_root.login_support.LoginSupportComponent.Event
import com.brsv44n.some_courier.presentation.login_root.login_support.store.LoginSupportStoreFactory
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import me.tatarka.inject.annotations.Inject

internal class LoginSupportComponentImpl(
    private val dependencies: LoginRootDependencies,
    private val output: (LoginSupportComponent.Output) -> Unit,
    private val componentContext: ComponentContext,
) : LoginSupportComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        LoginSupportStoreFactory(
            dispatchers = dependencies.dispatchers,
            storeFactory = dependencies.storeFactory,
            settingsRepository = dependencies.settingsRepository
        ).provide()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.CallManagerClicked -> {
                store.state.managersPhone?.let { dependencies.externalAppNavigator.openDialApp(it) }
                if (store.state.managersPhone == null) {
                    dependencies.externalAppNavigator.openDialApp(
                        dependencies.resourceManager.getString(
                            R.string.data_default_support_number
                        )
                    )
                }
            }

            Event.BackClicked -> {
                output.invoke(LoginSupportComponent.Output.Exit)
            }
        }
    }
}

@Inject
class LoginSupportComponentFactory(
    private val dependencies: LoginRootDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
        output: (LoginSupportComponent.Output) -> Unit,
    ): LoginSupportComponent = LoginSupportComponentImpl(
        dependencies = dependencies,
        output = output,
        componentContext = componentContext
    )
}
