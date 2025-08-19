package com.brsv44n.some_courier.di.dependencies

import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.use_case.LogoutUseCase
import com.brsv44n.some_courier.presentation.drawer.DrawerDependencies
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Inject

@Inject
class DrawerDependenciesImpl(
    override val storeFactory: StoreFactory,
    override val dispatchers: CoroutineDispatchers,
    override val profileRepository: ProfileRepository,
    override val errorHandler: ErrorHandler,
    override val authHolder: AuthHolder,
    override val logoutUseCase: LogoutUseCase
) : DrawerDependencies
