package com.brsv44n.some_courier.presentation.order

import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.models.toOrderUiModel
import com.brsv44n.some_courier.presentation.order.store.OrderStore
import com.brsv44n.some_courier.presentation.order.store.OrderStoreFactory
import com.brsv44n.some_courier.core.presentation.asValue
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.brsv44n.some_courier.core.utils.Text
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

internal class OrderDetailsComponentImpl(
    private val dependencies: OrderDetailsDependencies,
    private val componentContext: ComponentContext,
    private val orderId: Long,
    private val output: (OrderComponent.Output) -> Unit,
) : OrderComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        OrderStoreFactory(
            dispatchers = dependencies.dispatchers,
            storeFactory = dependencies.storeFactory,
            orderRepository = dependencies.orderRepository,
            profileRepository = dependencies.profileRepository,
            orderId = orderId
        ).provide()
    }

    private val scope = coroutineScope(dependencies.dispatchers.main + SupervisorJob())

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)
    }

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    override val isRefreshing: Value<Boolean>
        get() = store.asValue().map {
            it.isRefreshing
        }
    override val uiState: Value<OrderState>
        get() = store.asValue().map {
            when {
                it.isLoading -> OrderState.Loading
                it.error != null -> OrderState.Error(dependencies.errorHandler.getErrorModel(it.error))
                it.order != null -> OrderState.Success(
                    order = it.order.toOrderUiModel(dependencies.dateTimeFormatter),
                    isIssueButtonEnabled = it.order.canBeMarkedProblem,
                    isMapButtonEnabled = it.order.canBeOpenedInMap,
                    isOverlayProgressVisible = it.isOrderStatusChanging,
                )

                else -> OrderState.Error(ErrorModel.Unknown)
            }
        }

    override val modalState: MutableValue<OrderActiveModal> = MutableValue(OrderActiveModal.NONE)

    @Suppress("CyclomaticComplexMethod")
    override fun onEvent(event: OrderComponent.Event) {
        when (event) {
            OrderComponent.Event.BackClicked -> {
                this.output(OrderComponent.Output.Exit)
            }

            OrderComponent.Event.FinishOrderClicked -> {
                modalState.value = OrderActiveModal.FINISH
            }

            OrderComponent.Event.FinishOrderConfirmed -> {
                store.accept(OrderStore.Intent.UpdateOrderStatus)
                modalState.value = OrderActiveModal.NONE
            }

            OrderComponent.Event.ModalDismissed -> {
                modalState.value = OrderActiveModal.NONE
            }

            OrderComponent.Event.WarningClicked -> {
                modalState.value = OrderActiveModal.PROBLEM
            }

            OrderComponent.Event.WarningConfirmed -> {
                store.accept(OrderStore.Intent.MarkAsWithProblem)
                modalState.value = OrderActiveModal.NONE
            }

            is OrderComponent.Event.ChangeOrderStatusClicked -> {
                store.accept(OrderStore.Intent.UpdateOrderStatus)
            }

            is OrderComponent.Event.ClientClicked -> {
                store.state.order?.let {
                    dependencies.externalAppNavigator.openDialApp(phone = it.recipientInfo.phone)
                }
            }

            is OrderComponent.Event.ManagerClicked -> {
                store.state.user?.let {
                    dependencies.externalAppNavigator.openDialApp(phone = it.restaurant.managerPhone)
                }
            }

            is OrderComponent.Event.MapClicked -> {
                store.state.order?.recipientInfo?.location?.let { point ->
                    dependencies.externalAppNavigator.openRouteSinglePoint(
                        point = point
                    )
                }
            }

            OrderComponent.Event.RetryClicked -> {
                store.accept(OrderStore.Intent.Retry)
            }

            OrderComponent.Event.RefreshPulled -> {
                store.accept(OrderStore.Intent.Refresh)
            }
        }
    }

    private fun handleLabel(label: OrderStore.Label) {
        when (label) {
            is OrderStore.Label.FailedLabel -> {
                dependencies.messageNotifier.showError(
                    title = Text.Resource(R.string.label_error),
                    message = dependencies.errorHandler.getErrorMessage(label.error)
                )
            }

            OrderStore.Label.StatusUpdatingSucceed -> {
                dependencies.messageNotifier.showDone(
                    title = dependencies.resourceManager.getString(R.string.title_notification_order_delivered),
                    message = dependencies.resourceManager.getString(R.string.label_notification_order_updated)
                )
            }

            OrderStore.Label.MarkAsWithProblemSucceed -> {
                dependencies.messageNotifier.showDone(
                    title = dependencies.resourceManager.getString(R.string.title_notification_order_with_problem),
                    message = dependencies.resourceManager.getString(R.string.label_notification_order_updated)
                )
            }
        }
    }
}

@Inject
class OrderDetailsComponentFactory(
    private val dependencies: OrderDetailsDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
        orderId: Long,
        output: (OrderComponent.Output) -> Unit,
    ): OrderComponent = OrderDetailsComponentImpl(
        dependencies = dependencies,
        output = output,
        componentContext = componentContext,
        orderId = orderId,
    )
}
