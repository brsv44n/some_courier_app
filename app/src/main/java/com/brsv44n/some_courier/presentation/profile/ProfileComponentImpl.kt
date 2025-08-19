package com.brsv44n.some_courier.presentation.profile

import com.brsv44n.some_courier.presentation.models.toDomain
import com.brsv44n.some_courier.presentation.profile.store.ProfileStore
import com.brsv44n.some_courier.presentation.profile.store.ProfileStoreFactory
import com.brsv44n.some_courier.core.presentation.asValue
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import me.tatarka.inject.annotations.Inject

internal class ProfileComponentImpl(
    componentContext: ComponentContext,
    private val dependencies: ProfileDependencies,
) : ProfileComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        ProfileStoreFactory(
            dispatchers = dependencies.dispatchers,
            storeFactory = dependencies.storeFactory,
            profileRepository = dependencies.profileRepository
        ).provide()
    }

    override val uiState: Value<ProfileComponent.UiState>
        get() = store.asValue().map {
            ProfileComponent.UiState(
                state = when {
                    it.isLoading -> ProfileState.Loading
                    it.error != null -> ProfileState.Error(
                        dependencies.errorHandler.getErrorModel(
                            it.error
                        )
                    )

                    it.user != null -> ProfileState.Success(it.user.toDomain())
                    else -> ProfileState.Error(ErrorModel.Unknown)

                }
            )
        }

    override fun retryClicked() {
        store.accept(ProfileStore.Intent.Retry)
    }
}

@Inject
class ProfileComponentFactory(
    private val dependencies: ProfileDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
    ): ProfileComponent {
        return ProfileComponentImpl(
            componentContext = componentContext,
            dependencies = dependencies
        )
    }
}
