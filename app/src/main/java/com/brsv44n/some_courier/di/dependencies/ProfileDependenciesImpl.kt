package com.brsv44n.some_courier.di.dependencies

import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.presentation.profile.ProfileDependencies
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileDependenciesImpl(
    override val storeFactory: StoreFactory,
    override val dispatchers: CoroutineDispatchers,
    override val profileRepository: ProfileRepository,
    override val errorHandler: ErrorHandler
) : ProfileDependencies
