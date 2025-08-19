package com.brsv44n.some_courier.presentation.settings.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.permissions.PermissionsComponent
import com.brsv44n.some_courier.presentation.permissions.PreviewPermissionsComponent
import com.brsv44n.some_courier.presentation.ui_kit.ChpdPrimaryButton
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.orderComment
import com.brsv44n.some_courier.theme.strokeEnabled
import com.brsv44n.some_courier.theme.textTitle
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    component: PermissionsComponent,
) {
    val uiState by component.uiState.subscribeAsState()

    val activity = LocalContext.current as Activity
    LaunchedEffect(Unit) {
        component.resolvableExceptionHandlingFlow
            .onEach { it.resolve(activity, 0) }
            .launchIn(this)
    }

    val locationPermission = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        onPermissionsResult = {
            if (it[android.Manifest.permission.ACCESS_FINE_LOCATION] == false) {
                component.onFineLocationPermissionDenied()
            }
        }
    )

    val notificationPermission = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS,
        onPermissionResult = {
            if (!it) {
                component.onNotificationPermissionDenied()
            }
        }
    )

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        SettingsRow(
            modifier = Modifier.fillMaxWidth(),
            isEnabled = !uiState.isGpsEnabled,
            title = stringResource(R.string.title_settings_geo),
            label = stringResource(R.string.label_settings_geo),
            onClick = { component.onTurnOnGpsClicked() }
        )
        HorizontalDivider(
            modifier = Modifier,
            thickness = 1.dp,
            color = strokeEnabled
        )
        SettingsRow(
            modifier = Modifier.fillMaxWidth(),
            isEnabled = !uiState.isLocationPermissionGranted,
            title = stringResource(R.string.title_settings_geo_permission),
            label = stringResource(R.string.label_settings_geo_permission),
            onClick = { locationPermission.launchMultiplePermissionRequest() }
        )
        HorizontalDivider(
            modifier = Modifier,
            thickness = 1.dp,
            color = strokeEnabled
        )
        SettingsRow(
            modifier = Modifier.fillMaxWidth(),
            isEnabled = !uiState.isNotificationsPermissionGranted,
            title = stringResource(R.string.title_settings_push_permission),
            label = stringResource(R.string.label_settings_push_permission),
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermission.launchPermissionRequest()
                } else {
                    component.onNotificationPermissionDenied()
                }
            }
        )
    }
}

@Composable
private fun SettingsRow(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    title: String,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = TextStyles.AndroidBodyLarge,
                color = textTitle
            )
            Text(
                text = label,
                style = TextStyles.AndroidBodySmall,
                color = orderComment
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        ChpdPrimaryButton(
            modifier = Modifier,
            enabled = isEnabled,
            shape = RoundedCornerShape(8.dp),
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp)
        ) {
            val buttonText: String =
                if (isEnabled) {
                    stringResource(R.string.action_settings_button_enabled)
                } else {
                    stringResource(R.string.action_settings_button_disabled)
                }
            Text(
                text = buttonText,
                style = TextStyles.AndroidLabelMediumProminent
            )
        }
    }
}

@Composable
@Preview
private fun PreviewSettingsContent() {
    CHPAndroidCourierTheme {
        SettingsContent(
            modifier = Modifier.fillMaxSize(),
            component = PreviewPermissionsComponent()
        )
    }
}
