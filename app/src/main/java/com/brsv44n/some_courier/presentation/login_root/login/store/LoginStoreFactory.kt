package com.brsv44n.some_courier.presentation.login_root.login.store

import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.domain.error.LoginValidationError
import com.brsv44n.some_courier.domain.use_case.LoginUseCase
import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStore.Intent
import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStore.Label
import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStore.State
import com.brsv44n.some_courier.core.network.error.ApiError
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch

internal class LoginStoreFactory(
    private val dispatchers: CoroutineDispatchers,
    private val storeFactory: StoreFactory,
    private val loginUseCase: LoginUseCase,
    private val resourceManager: ResourceManager,
) {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): LoginStore = object :
        LoginStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "login_store",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory(mainContext = dispatchers.main) {
                onIntent<Intent.LoginClicked> {
                    launch { login() }
                }

                onIntent<Intent> {
                    dispatch(Msg.Intent(it))
                }
            },
            reducer = ReducerImpl
        ) {}

    @OptIn(ExperimentalMviKotlinApi::class)
    private suspend fun CoroutineExecutorScope<State, Msg, Label>.login() {
        dispatch(Msg.LoadingStarted)
        loginUseCase(state.phone)
            .fold(
                onSuccess = {
                    publish(Label.LoginSucceed)
                },
                onFailure = {
                    if (it is LoginValidationError || (it is ApiError && it.code == 403)) {
                        dispatch(
                            Msg.CredentialsValidationFailed(
                                it.message ?: resourceManager.getString(
                                    R.string.error_login_screen_phone_number
                                )
                            )
                        )
                    } else {
                        publish(Label.LoginFailed(it))
                    }
                }
            )
        dispatch(Msg.LoadingFinished)
    }

    private sealed interface Msg {
        data class Intent(val value: LoginStore.Intent) : Msg
        data class CredentialsValidationFailed(val errorText: String) : Msg
        data object LoadingFinished : Msg
        data object LoadingStarted : Msg
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.Intent -> reduce(msg.value)
            is Msg.CredentialsValidationFailed -> copy(
                showInputError = true,
                invalidPhoneError = msg.errorText
            )

            Msg.LoadingFinished -> copy(isLoginInProgress = false)
            Msg.LoadingStarted -> copy(isLoginInProgress = true)
        }

        private fun State.reduce(intent: Intent): State = when (intent) {
            is Intent.PhoneChanged -> {
                if (intent.phone.length > 10) {
                    copy(invalidPhoneError = "")
                } else {
                    copy(
                        phone = intent.phone.filter { !it.isWhitespace() && it.isDigit() },
                        showInputError = false,
                        invalidPhoneError = ""
                    )
                }
            }

            is Intent.ClearClicked -> {
                copy(phone = "")
            }

            else -> {
                this
            }
        }
    }
}
