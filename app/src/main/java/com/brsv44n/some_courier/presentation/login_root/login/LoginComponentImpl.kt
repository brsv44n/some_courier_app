package com.brsv44n.some_courier.presentation.login_root.login

import com.brsv44n.some_courier.domain.use_case.LoginUseCase
import com.brsv44n.some_courier.presentation.login_root.LoginRootDependencies
import com.brsv44n.some_courier.presentation.login_root.login.LoginComponent.UiState
import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStore
import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStore.Intent
import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStoreFactory
import com.brsv44n.some_courier.core.presentation.asValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

internal class LoginComponentImpl(
    private val dependencies: LoginRootDependencies,
    private val output: (LoginComponent.Output) -> Unit,
    private val componentContext: ComponentContext,
    private val loginUseCase: LoginUseCase,
) : LoginComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        LoginStoreFactory(
            dispatchers = dependencies.dispatchers,
            storeFactory = dependencies.storeFactory,
            loginUseCase = loginUseCase,
            resourceManager = dependencies.resourceManager
        ).provide()
    }

    private val scope = coroutineScope(dependencies.dispatchers.main + SupervisorJob())

    override val uiState: Value<UiState>
        get() = store.asValue().map {
            UiState(
                phone = it.phone,
                isClearButtonVisible = it.phone.isNotEmpty(),
                isLoginButtonEnabled = it.canLogin && !it.isLoginInProgress,
                isInputsEnabled = !it.isLoginInProgress,
                showInputError = it.showInputError,
                isLoginProgressVisible = it.isLoginInProgress,
                invalidPhoneError = it.invalidPhoneError
            )
        }

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)
    }

    override fun onEvent(event: LoginComponent.Event) {
        when (event) {
            is LoginComponent.Event.PhoneChanged -> store.accept(Intent.PhoneChanged(event.phone))
            LoginComponent.Event.LoginClicked -> store.accept(Intent.LoginClicked)
            LoginComponent.Event.SupportClicked -> output(LoginComponent.Output.LoginSupport)
            LoginComponent.Event.ClearClicked -> store.accept(Intent.ClearClicked)
        }
    }

    private fun handleLabel(label: LoginStore.Label) {
        when (label) {
            is LoginStore.Label.LoginSucceed -> {
                output.invoke(LoginComponent.Output.LoginSucceed)
            }

            is LoginStore.Label.LoginFailed -> {
                dependencies.messageNotifier.showError(
                    dependencies.errorHandler.getErrorMessage(
                        label.error
                    )
                )
            }
        }
    }
}

@Inject
class LoginComponentFactory(
    private val dependencies: LoginRootDependencies,
    private val loginUseCase: LoginUseCase,
) {
    fun invoke(
        componentContext: ComponentContext,
        output: (LoginComponent.Output) -> Unit,
    ): LoginComponent = LoginComponentImpl(
        dependencies = dependencies,
        loginUseCase = loginUseCase,
        output = output,
        componentContext = componentContext
    )
}
