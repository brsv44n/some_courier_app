package com.brsv44n.some_courier.presentation.in_process_orders

import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.domain.models.RouteStatus
import com.brsv44n.some_courier.presentation.in_process_orders.store.InProcessOrdersStore
import com.brsv44n.some_courier.presentation.in_process_orders.store.InProcessOrdersStoreFactory
import com.brsv44n.some_courier.presentation.models.toOrderCardUiModel
import com.brsv44n.some_courier.core.presentation.asValue
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.brsv44n.some_courier.core.utils.Text
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class InProcessOrdersComponentImpl(
    componentContext: ComponentContext,
    private val dependencies: InProcessOrdersDependencies,
    private val output: (InProcessOrdersComponent.Output) -> Unit,
) : InProcessOrdersComponent, ComponentContext by componentContext {

    override val showModal: MutableValue<Boolean> = MutableValue(false)

    private val store = instanceKeeper.getStore {
        InProcessOrdersStoreFactory(
            dispatchers = dependencies.dispatchers,
            storeFactory = dependencies.storeFactory,
            routesRepository = dependencies.routesRepository,
            profileRepository = dependencies.profileRepository,
            ordersListRepository = dependencies.ordersListRepository
        ).provide()
    }

    private val scope = coroutineScope(dependencies.dispatchers.main + SupervisorJob())

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)
        lifecycle.doOnResume {
            store.accept(InProcessOrdersStore.Intent.ViewResumed)
        }
        lifecycle.doOnDestroy {
            store.accept(InProcessOrdersStore.Intent.ViewDestroyed)
        }
    }

    override val ordersState: Value<OrdersInProcessState>
        get() = store.asValue().map { handleOrdersState(it) }

    override fun onEvent(event: InProcessOrdersComponent.Event) {
        when (event) {
            InProcessOrdersComponent.Event.OpenRouteClicked -> {
                store.state.route?.orders?.map { it.recipientInfo.location }?.let {
                    dependencies.externalAppNavigator.openRouteMultiplePoints(
                        points = it
                    )
                }
            }

            InProcessOrdersComponent.Event.RetryClicked -> {
                store.accept(InProcessOrdersStore.Intent.Retry)
            }

            is InProcessOrdersComponent.Event.OrderClicked -> {
                output(InProcessOrdersComponent.Output.OrderClicked(event.orderId))
            }

            InProcessOrdersComponent.Event.ModalDismissed -> {
                showModal.value = false
            }

            InProcessOrdersComponent.Event.EndRouteClicked -> {
                showModal.value = true
            }

            InProcessOrdersComponent.Event.EndRouteConfirmed -> {
                store.accept(InProcessOrdersStore.Intent.CloseRoute)
                showModal.value = false
            }

            InProcessOrdersComponent.Event.RefreshPulled -> {
                store.accept(InProcessOrdersStore.Intent.Refresh)
            }
        }
    }

    private fun handleLabel(label: InProcessOrdersStore.Label) {
        when (label) {
            is InProcessOrdersStore.Label.FailedLabel -> {
                dependencies.messageNotifier.showError(
                    title = Text.Resource(R.string.label_error),
                    message = dependencies.errorHandler.getErrorMessage(label.error)
                )
            }

            is InProcessOrdersStore.Label.ClosingRouteSucceed -> {
                dependencies.messageNotifier.showDone(
                    title = Text.Resource(R.string.title_notification_route_closed_successfully),
                    message = Text.Resource(R.string.label_notification_route_closed_successfully)
                )
            }
        }
    }

    private fun handleOrdersState(storeState: InProcessOrdersStore.State): OrdersInProcessState {
        return when {
            storeState.route != null &&
                    storeState.restaurantAddress != null &&
                    storeState.route.orders.isNotEmpty() -> OrdersInProcessState.Success(
                userAddress = storeState.restaurantAddress,
                ordersList = storeState.route.orders.map { order ->
                    order.toOrderCardUiModel(
                        dependencies.dateTimeFormatter
                    )
                },
                showEndRouteButton = storeState.route.status == RouteStatus.Delivered,
                isRefreshing = storeState.isRouteRefreshing,
                isRouteStatusUpdating = storeState.isRouteStatusUpdating
            )

            storeState.isLoading -> OrdersInProcessState.Loading

            storeState.error != null -> OrdersInProcessState.Error(
                error = dependencies.errorHandler.getErrorModel(
                    storeState.error
                ),
                isRefreshing = false
            )

            storeState.route == null || storeState.route.orders.isEmpty() -> OrdersInProcessState.Empty(
                isRefreshing = storeState.isRouteRefreshing,
                isRouteStatusUpdating = storeState.isRouteStatusUpdating
            )

            storeState.profileError != null -> OrdersInProcessState.Error(
                dependencies.errorHandler.getErrorModel(
                    storeState.profileError
                )
            )

            else -> OrdersInProcessState.Error(ErrorModel.Unknown)
        }
    }
}

@Inject
class InProcessOrdersComponentFactory(
    private val dependencies: InProcessOrdersDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
        output: (InProcessOrdersComponent.Output) -> Unit,
    ): InProcessOrdersComponent = InProcessOrdersComponentImpl(
        componentContext = componentContext,
        output = output,
        dependencies = dependencies
    )
}
