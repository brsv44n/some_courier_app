package com.brsv44n.some_courier.presentation.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.profile.PreviewProfileComponent
import com.brsv44n.some_courier.presentation.profile.ProfileComponent
import com.brsv44n.some_courier.presentation.profile.ProfileState
import com.brsv44n.some_courier.presentation.ui_kit.ErrorScreen
import com.brsv44n.some_courier.presentation.ui_kit.ProgressLoader
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.orderComment
import com.brsv44n.some_courier.theme.strokeEnabled
import com.brsv44n.some_courier.theme.strokesPrimary
import com.brsv44n.some_courier.theme.textTitle
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
internal fun ProfileContent(
    modifier: Modifier = Modifier,
    component: ProfileComponent,
) {
    val uiState by component.uiState.subscribeAsState()

    when (val state = uiState.state) {
        ProfileState.Loading -> {
            Box(modifier) {
                ProgressLoader(modifier = Modifier.align(Alignment.Center))
            }
        }

        is ProfileState.Error -> {
            ErrorScreen(
                modifier = Modifier.fillMaxSize(),
                onClick = component::retryClicked,
                errorType = state.error
            )
        }

        is ProfileState.Success -> {
            Column(
                modifier = modifier
                    .background(color = strokesPrimary)
                    .padding(16.dp),
            ) {
                Text(
                    text = state.userEntity.name,
                    style = TextStyles.AndroidHeadlineMedium,
                    color = textTitle
                )
                Spacer(modifier = Modifier.height(16.dp))
                ProfileInfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.label_profile_restaurant),
                    value = state.userEntity.currentRestaurant.let {
                        it ?: stringResource(R.string.label_not_selected_field)
                    }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = strokeEnabled
                )
                ProfileInfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.label_profile_unique_id),
                    value = state.userEntity.id.toString()
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = strokeEnabled
                )
                ProfileInfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.label_profile_phone),
                    value = state.userEntity.phone.let {
                        it ?: stringResource(R.string.label_not_selected_field)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
) {
    Row(
        modifier = modifier
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = TextStyles.AndroidBodyLarge,
            color = orderComment
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = TextStyles.AndroidBodyLarge,
            color = textTitle
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewProfileContent() {
    CHPAndroidCourierTheme {
        ProfileContent(
            modifier = Modifier.fillMaxSize(),
            component = PreviewProfileComponent()
        )
    }
}

@Composable
@Preview
private fun PreviewProfileInfoRow() {
    CHPAndroidCourierTheme {
        ProfileInfoRow(
            modifier = Modifier.fillMaxWidth(),
            title = "Title",
            value = "Value"
        )
    }
}
