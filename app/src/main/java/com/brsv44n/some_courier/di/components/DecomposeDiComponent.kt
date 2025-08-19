package com.brsv44n.some_courier.di.components

import com.brsv44n.some_courier.data.OrdersDataRepository
import com.brsv44n.some_courier.data.ProfileDataRepository
import com.brsv44n.some_courier.data.RoutesDataRepository
import com.brsv44n.some_courier.data.SettingsDataRepository
import com.brsv44n.some_courier.di.dependencies.AuthenticatedUserRootDependenciesImpl
import com.brsv44n.some_courier.di.dependencies.DrawerDependenciesImpl
import com.brsv44n.some_courier.di.dependencies.InProcessOrdersDependenciesImpl
import com.brsv44n.some_courier.di.dependencies.LoginRootDependenciesImpl
import com.brsv44n.some_courier.di.dependencies.OrderDetailsDependenciesImpl
import com.brsv44n.some_courier.di.dependencies.OrdersHistoryDependenciesImpl
import com.brsv44n.some_courier.di.dependencies.ProfileDependenciesImpl
import com.brsv44n.some_courier.di.dependencies.RootComponentDependenciesImpl
import com.brsv44n.some_courier.domain.repository.OrderRepository
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.repository.RoutesRepository
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import com.brsv44n.some_courier.presentation.authenticated_user_root.AuthenticatedUserRootComponentFactory
import com.brsv44n.some_courier.presentation.authenticated_user_root.AuthenticatedUserRootDependencies
import com.brsv44n.some_courier.presentation.drawer.DrawerComponentFactory
import com.brsv44n.some_courier.presentation.drawer.DrawerDependencies
import com.brsv44n.some_courier.presentation.in_process_orders.InProcessOrdersComponentFactory
import com.brsv44n.some_courier.presentation.in_process_orders.InProcessOrdersDependencies
import com.brsv44n.some_courier.presentation.login_root.LoginRootComponentFactory
import com.brsv44n.some_courier.presentation.login_root.LoginRootDependencies
import com.brsv44n.some_courier.presentation.order.OrderDetailsComponentFactory
import com.brsv44n.some_courier.presentation.order.OrderDetailsDependencies
import com.brsv44n.some_courier.presentation.orders_history.OrdersHistoryComponentFactory
import com.brsv44n.some_courier.presentation.orders_history.OrdersHistoryDependencies
import com.brsv44n.some_courier.presentation.profile.ProfileComponentFactory
import com.brsv44n.some_courier.presentation.profile.ProfileDependencies
import com.brsv44n.some_courier.presentation.root.RootComponentDependencies
import com.brsv44n.some_courier.presentation.root.RootComponentFactory
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.popup.MessageNotifier
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class DecomposeDiComponent(
    @Component val appDiComponent: AppDiComponent,
) {

    abstract val messageNotifier: MessageNotifier

    abstract val rootComponentFactory: RootComponentFactory

    abstract val loginRootComponentFactory: LoginRootComponentFactory

    abstract val drawerComponentFactory: DrawerComponentFactory

    abstract val profileComponentFactory: ProfileComponentFactory

    abstract val authenticatedUserRootComponentFactory: AuthenticatedUserRootComponentFactory

    abstract val ordersHistoryComponentFactory: OrdersHistoryComponentFactory

    abstract val orderDetailsComponentFactory: OrderDetailsComponentFactory

    abstract val inProcessOrdersComponentFactory: InProcessOrdersComponentFactory

    @Singleton
    abstract val messagingTokenProvider: MessagingTokenProvider

    @Provides
    fun LoginRootDependenciesImpl.bind(): LoginRootDependencies = this

    @Provides
    fun RootComponentDependenciesImpl.bind(): RootComponentDependencies = this

    @Provides
    fun DrawerDependenciesImpl.bind(): DrawerDependencies = this

    @Provides
    fun ProfileDependenciesImpl.bind(): ProfileDependencies = this

    @Provides
    fun AuthenticatedUserRootDependenciesImpl.bind(): AuthenticatedUserRootDependencies = this

    @Provides
    fun OrdersHistoryDependenciesImpl.bind(): OrdersHistoryDependencies = this

    @Provides
    fun OrderDetailsDependenciesImpl.bind(): OrderDetailsDependencies = this

    @Provides
    fun InProcessOrdersDependenciesImpl.bind(): InProcessOrdersDependencies = this

    @Provides
    fun SettingsDataRepository.bind(): SettingsRepository = this

    @Provides
    fun ProfileDataRepository.bind(): ProfileRepository = this

    @Provides
    fun OrdersDataRepository.bind(): OrderRepository = this

    @Provides
    fun RoutesDataRepository.bind(): RoutesRepository = this

}
