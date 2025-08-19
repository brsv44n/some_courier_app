package com.brsv44n.some_courier.presentation.profile

import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface ProfileDependencies {
    val storeFactory: StoreFactory
    val dispatchers: CoroutineDispatchers
    val profileRepository: ProfileRepository
    val errorHandler: ErrorHandler
}
