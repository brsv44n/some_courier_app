package com.brsv44n.some_courier.presentation.login_root.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.brsv44n.some_courier.presentation.login_root.LoginRootComponent
import com.brsv44n.some_courier.presentation.login_root.login.ui.LoginContent
import com.brsv44n.some_courier.presentation.login_root.login_support.ui.LoginSupportContent
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
internal fun LoginRootContent(
    modifier: Modifier = Modifier,
    component: LoginRootComponent
) {
    val stack by component.childStack.subscribeAsState()

    Children(
        modifier = modifier,
        stack = stack,
        animation = stackAnimation(slide())
    ) { child ->
        when (val activeChild = child.instance) {

            is LoginRootComponent.Child.Login -> {
                LoginContent(
                    modifier = Modifier.fillMaxSize(),
                    component = activeChild.component
                )
            }

            is LoginRootComponent.Child.Support -> {
                LoginSupportContent(
                    modifier = Modifier.fillMaxSize(),
                    component = activeChild.component
                )
            }
        }
    }
}
