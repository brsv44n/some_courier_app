package com.brsv44n.some_courier.di.components

import com.brsv44n.some_courier.data.LoginDataRepository
import com.brsv44n.some_courier.domain.repository.LoginRepository
import com.brsv44n.some_courier.domain.use_case.LoginUseCase
import com.brsv44n.some_courier.presentation.login_root.LoginRootDependencies
import com.brsv44n.some_courier.presentation.login_root.login.LoginComponentFactory
import com.brsv44n.some_courier.presentation.login_root.login_support.LoginSupportComponentFactory
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Singleton
@Component
abstract class LoginDiComponent(
    @Component @get:Provides val dependencies: LoginRootDependencies,
) : InstanceKeeper.Instance {

    abstract val loginComponentFactory: LoginComponentFactory

    abstract val loginSupportComponentFactory: LoginSupportComponentFactory

    abstract val loginUseCase: LoginUseCase

    @Singleton
    @Provides
    fun LoginDataRepository.bind(): LoginRepository = this

}
