package com.brsv44n.some_courier.di.dependencies

import com.brsv44n.some_courier.domain.session.UnauthorizedErrorEmitter
import com.brsv44n.some_courier.domain.use_case.LogoutUseCase
import com.brsv44n.some_courier.presentation.authenticated_user_root.AuthenticatedUserRootComponentFactory
import com.brsv44n.some_courier.presentation.location.LocationServiceLauncherComponentFactory
import com.brsv44n.some_courier.presentation.login_root.LoginRootComponentFactory
import com.brsv44n.some_courier.presentation.order.OrderDetailsComponentFactory
import com.brsv44n.some_courier.presentation.root.RootComponentDependencies
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Inject

@Inject
class RootComponentDependenciesImpl(
    override val loginFactory: LoginRootComponentFactory,
    override val authenticatedUserRootComponentFactory: AuthenticatedUserRootComponentFactory,
    override val orderDetailsComponentFactory: OrderDetailsComponentFactory,
    override val storeFactory: StoreFactory,
    override val dispatchers: CoroutineDispatchers,
    override val authHolder: AuthHolder,
    override val locationServiceLauncherComponentFactory: LocationServiceLauncherComponentFactory,
    override val unauthorizedErrorEmitter: UnauthorizedErrorEmitter,
    override val logoutUseCase: LogoutUseCase
) : RootComponentDependencies
