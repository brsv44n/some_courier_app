package com.brsv44n.some_courier.presentation.drawer

import com.brsv44n.some_courier.presentation.drawer.store.DrawerStore
import com.brsv44n.some_courier.presentation.drawer.store.DrawerStoreFactory
import com.brsv44n.some_courier.presentation.models.toDomain
import com.brsv44n.some_courier.core.presentation.asValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

internal class DrawerComponentImpl(
    componentContext: ComponentContext,
    private val dependencies: DrawerDependencies,
    private val output: (DrawerComponent.Output) -> Unit,
) : DrawerComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        DrawerStoreFactory(
            dispatchers = dependencies.dispatchers,
            storeFactory = dependencies.storeFactory,
            profileRepository = dependencies.profileRepository,
            logoutUseCase = dependencies.logoutUseCase
        ).provide()
    }

    private val scope = coroutineScope(dependencies.dispatchers.main + SupervisorJob())

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)
    }

    override val uiState: Value<DrawerComponent.UiState>
        get() = store.asValue().map {
            when {
                it.isLoading -> DrawerComponent.UiState(isLoading = true)
                it.error != null -> DrawerComponent.UiState(
                    error = dependencies.errorHandler.getErrorModel(
                        it.error
                    )
                )

                it.user != null -> DrawerComponent.UiState(currentUser = it.user.toDomain())
                else -> error("Unknown state")
            }
        }

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    override val isProgressVisible: Value<Boolean>
        get() = store.asValue().map { it.isLoggingOut }

    override val modalState: MutableValue<DrawerActiveModal> = MutableValue(DrawerActiveModal.NONE)

    override fun onEvent(event: DrawerComponent.Event) =
        when (event) {
            DrawerComponent.Event.LogoutClicked -> {
                modalState.value = DrawerActiveModal.LOGOUT
            }

            DrawerComponent.Event.HistoryClicked -> {
                output.invoke(DrawerComponent.Output.HistoryClicked)
            }

            DrawerComponent.Event.ProfileClicked -> {
                output.invoke(DrawerComponent.Output.ProfileClicked)
            }

            DrawerComponent.Event.OrdersClicked -> {
                output.invoke(DrawerComponent.Output.OrdersClicked)
            }

            DrawerComponent.Event.SettingsClicked -> {
                output.invoke(DrawerComponent.Output.SettingsClicked)
            }

            DrawerComponent.Event.LogoutConfirmed -> {
                modalState.value = DrawerActiveModal.NONE
                store.accept(DrawerStore.Intent.LogoutClicked)
            }

            DrawerComponent.Event.ModalDismissed -> {
                modalState.value = DrawerActiveModal.NONE
            }
        }

    private fun handleLabel(label: DrawerStore.Label) {
        when (label) {
            is DrawerStore.Label.LoggedOut -> {
                output.invoke(DrawerComponent.Output.OpenLogin)
            }
        }
    }
}

@Inject
class DrawerComponentFactory(
    private val dependencies: DrawerDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
        output: (DrawerComponent.Output) -> Unit,
    ): DrawerComponent {
        return DrawerComponentImpl(
            componentContext = componentContext,
            output = output,
            dependencies = dependencies
        )
    }
}
