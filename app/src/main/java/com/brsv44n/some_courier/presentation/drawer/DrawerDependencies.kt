package com.brsv44n.some_courier.presentation.drawer

import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.use_case.LogoutUseCase
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface DrawerDependencies {
    val storeFactory: StoreFactory
    val dispatchers: CoroutineDispatchers
    val profileRepository: ProfileRepository
    val errorHandler: ErrorHandler
    val authHolder: AuthHolder
    val logoutUseCase: LogoutUseCase
}
