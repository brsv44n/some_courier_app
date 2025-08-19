package com.brsv44n.some_courier.presentation.login_root

import com.brsv44n.some_courier.presentation.login_root.login.LoginComponent
import com.brsv44n.some_courier.presentation.login_root.login_support.LoginSupportComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface LoginRootComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed interface Output {
        data object Exit : Output
        data object LoginSucceed : Output
    }

    sealed interface Child {
        data class Login(val component: LoginComponent) : Child
        data class Support(val component: LoginSupportComponent) : Child
    }

}
