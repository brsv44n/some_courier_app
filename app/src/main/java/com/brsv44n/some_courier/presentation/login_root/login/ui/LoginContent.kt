package com.brsv44n.some_courier.presentation.login_root.login.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.login_root.login.LoginComponent
import com.brsv44n.some_courier.presentation.login_root.login.LoginComponent.Event
import com.brsv44n.some_courier.presentation.login_root.login.PreviewLoginComponent
import com.brsv44n.some_courier.presentation.ui_kit.ChpdBorderlessButton
import com.brsv44n.some_courier.presentation.ui_kit.ChpdPrimaryButton
import com.brsv44n.some_courier.presentation.ui_kit.ChpdTextField
import com.brsv44n.some_courier.presentation.ui_kit.NavigationToolbar
import com.brsv44n.some_courier.presentation.ui_kit.OverlayProgress
import com.brsv44n.some_courier.presentation.ui_kit.transformPhoneInput
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.Typography
import com.brsv44n.some_courier.theme.serviceError
import com.brsv44n.some_courier.theme.textBody
import com.brsv44n.some_courier.theme.textTitle
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    component: LoginComponent,
) {

    val uiState by component.uiState.subscribeAsState()

    if (uiState.isLoginProgressVisible) {
        OverlayProgress()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            NavigationToolbar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.some_courier_logo),
                        contentDescription = null
                    )
                }
            )
        },
    ) { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.padding(top = 64.dp))

            Text(
                text = stringResource(R.string.title_login_screen),
                style = TextStyles.AndroidHeadlineSmall,
                color = textTitle
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            Text(
                text = stringResource(R.string.label_login_screen),
                style = TextStyles.AndroidBodyLarge,
                color = textBody
            )

            Spacer(modifier = Modifier.padding(top = 24.dp))

            ChpdTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.phone,
                onValueChange = { component.onEvent(Event.PhoneChanged(it)) },
                label = { Text(stringResource(R.string.hint_login_screen_phone_number)) },
                enabled = uiState.isInputsEnabled,
                isError = uiState.showInputError,
                trailingIcon = {
                    AnimatedVisibility(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(20.dp),
                        visible = uiState.isClearButtonVisible
                    ) {
                        IconButton(
                            modifier = Modifier.size(20.dp),
                            onClick = { component.onEvent(Event.ClearClicked) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_clear),
                                contentDescription = null
                            )
                        }
                    }
                },
                visualTransformation = { transformPhoneInput(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            val supportTextAlpha by animateFloatAsState(
                targetValue = if (uiState.showInputError) 1f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "supportTextAlpha"
            )

            Text(
                modifier = Modifier
                    .alpha(supportTextAlpha)
                    .padding(top = 4.dp)
                    .padding(horizontal = 12.dp),
                text = uiState.invalidPhoneError,
                style = Typography.bodySmall,
                color = serviceError
            )

            Spacer(modifier = Modifier.height(20.dp))

            ChpdPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { component.onEvent(Event.LoginClicked) },
                content = {
                    Text(
                        stringResource(R.string.action_login_screen_login),
                        style = TextStyles.AndroidLabelLarge
                    )
                },
                enabled = uiState.isLoginButtonEnabled
            )

            Spacer(modifier = Modifier.height(12.dp))

            ChpdBorderlessButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { component.onEvent(Event.SupportClicked) },
                content = {
                    Text(text = stringResource(R.string.action_login_screen_support))
                }
            )
        }
    }
}

@Composable
@Preview
private fun PreviewLoginContent() {
    CHPAndroidCourierTheme {
        LoginContent(
            modifier = Modifier.fillMaxSize(),
            component = PreviewLoginComponent()
        )
    }
}
