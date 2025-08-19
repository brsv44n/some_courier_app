package com.brsv44n.some_courier.presentation.login_root.login_support.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.login_root.login_support.LoginSupportComponent
import com.brsv44n.some_courier.presentation.login_root.login_support.LoginSupportComponent.Event
import com.brsv44n.some_courier.presentation.login_root.login_support.PreviewLoginSupportComponent
import com.brsv44n.some_courier.presentation.ui_kit.ChpdOutlinedButton
import com.brsv44n.some_courier.presentation.ui_kit.NavigationBackToolbar
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.textBody
import com.brsv44n.some_courier.theme.textTitle

@Composable
internal fun LoginSupportContent(
    modifier: Modifier,
    component: LoginSupportComponent
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            NavigationBackToolbar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.some_courier_logo),
                        contentDescription = null
                    )
                },
                onBackClick = { component.onEvent(Event.BackClicked) }
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
                text = stringResource(R.string.title_login_support_screen),
                style = TextStyles.AndroidHeadlineSmall,
                color = textTitle
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            Text(
                text = stringResource(R.string.label_login_support_screen),
                style = TextStyles.AndroidBodyLarge,
                color = textBody
            )

            Spacer(modifier = Modifier.padding(top = 24.dp))

            ChpdOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp),
                onClick = { component.onEvent(Event.CallManagerClicked) },
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_support_agent),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(text = stringResource(R.string.action_login_support_screen_contact_manager))
                    }
                }
            )
        }
    }
}

@Composable
@Preview
private fun PreviewLoginSupportContent() {
    CHPAndroidCourierTheme {
        LoginSupportContent(
            modifier = Modifier.fillMaxSize(),
            component = PreviewLoginSupportComponent()
        )
    }
}
