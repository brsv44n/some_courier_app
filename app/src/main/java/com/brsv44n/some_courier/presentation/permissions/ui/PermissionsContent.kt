package com.brsv44n.some_courier.presentation.permissions.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import com.brsv44n.some_courier.theme.Typography
import com.brsv44n.some_courier.theme.orderComment
import com.brsv44n.some_courier.theme.strokesSecondary
import com.brsv44n.some_courier.theme.textBody
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
fun PermissionsContent(
    modifier: Modifier = Modifier,
    component: PermissionsComponent
) {
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

    Scaffold(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(top = 72.dp, bottom = 32.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.title_permissions),
                style = Typography.headlineSmall,
                color = textTitle
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.label_permissions_description),
                style = Typography.bodyLarge,
                color = textBody
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
            ) {
                val uiState by component.uiState.subscribeAsState()
                val rowModifier = Modifier.padding(vertical = 12.dp)
                PermissionsRow(
                    modifier = rowModifier,
                    title = stringResource(R.string.label_permission_location_permission),
                    description = stringResource(R.string.label_permission_location_permission_description),
                    isGranted = uiState.isGpsEnabled,
                    onActionClick = component::onTurnOnGpsClicked
                )
                HorizontalDivider(
                    modifier = Modifier,
                    thickness = 1.dp,
                    color = strokesSecondary
                )
                PermissionsRow(
                    modifier = rowModifier,
                    title = stringResource(R.string.label_permission_gps),
                    description = stringResource(R.string.label_permission_gps_description),
                    isGranted = uiState.isLocationPermissionGranted,
                    onActionClick = {
                        locationPermission.launchMultiplePermissionRequest()
                    }
                )
                HorizontalDivider(
                    modifier = Modifier,
                    thickness = 1.dp,
                    color = strokesSecondary
                )
                PermissionsRow(
                    modifier = rowModifier,
                    title = stringResource(R.string.label_permission_notifications_permission),
                    description = stringResource(R.string.label_permission_notifications_permission_description),
                    isGranted = uiState.isNotificationsPermissionGranted,
                    onActionClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermission.launchPermissionRequest()
                        } else {
                            component.onNotificationPermissionDenied()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PermissionsRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    isGranted: Boolean,
    onActionClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = Typography.bodyLarge,
                color = textTitle
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = Typography.bodySmall,
                color = orderComment
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        ChpdPrimaryButton(
            modifier = Modifier,
            enabled = !isGranted,
            shape = RoundedCornerShape(8.dp),
            onClick = onActionClick,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp)
        ) {
            val buttonText: String =
                if (isGranted) {
                    stringResource(R.string.action_permission_enabled)
                } else {
                    stringResource(R.string.action_permission_enable)
                }
            Text(
                text = buttonText,
                style = TextStyles.AndroidLabelMediumProminent
            )
        }
    }
}

@Preview
@Composable
private fun PermissionsContentPreview() {
    CHPAndroidCourierTheme {
        PermissionsContent(
            component = PreviewPermissionsComponent()
        )
    }
}
