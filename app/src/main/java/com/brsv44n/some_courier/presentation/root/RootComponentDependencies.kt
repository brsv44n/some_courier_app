package com.brsv44n.some_courier.presentation.root

import com.brsv44n.some_courier.domain.session.UnauthorizedErrorEmitter
import com.brsv44n.some_courier.domain.use_case.LogoutUseCase
import com.brsv44n.some_courier.presentation.authenticated_user_root.AuthenticatedUserRootComponentFactory
import com.brsv44n.some_courier.presentation.location.LocationServiceLauncherComponentFactory
import com.brsv44n.some_courier.presentation.login_root.LoginRootComponentFactory
import com.brsv44n.some_courier.presentation.order.OrderDetailsComponentFactory
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface RootComponentDependencies {
    val loginFactory: LoginRootComponentFactory
    val authenticatedUserRootComponentFactory: AuthenticatedUserRootComponentFactory
    val orderDetailsComponentFactory: OrderDetailsComponentFactory
    val locationServiceLauncherComponentFactory: LocationServiceLauncherComponentFactory
    val storeFactory: StoreFactory
    val dispatchers: CoroutineDispatchers
    val authHolder: AuthHolder
    val unauthorizedErrorEmitter: UnauthorizedErrorEmitter
    val logoutUseCase: LogoutUseCase
}
