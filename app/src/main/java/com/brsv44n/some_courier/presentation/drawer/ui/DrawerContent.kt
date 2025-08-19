package com.brsv44n.some_courier.presentation.drawer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.BuildConfig
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.drawer.DrawerActiveModal
import com.brsv44n.some_courier.presentation.drawer.DrawerComponent
import com.brsv44n.some_courier.presentation.drawer.DrawerComponent.Event
import com.brsv44n.some_courier.presentation.drawer.PreviewDrawerComponent
import com.brsv44n.some_courier.presentation.ui_kit.ConfirmationBottomSheet
import com.brsv44n.some_courier.presentation.ui_kit.NamedRow
import com.brsv44n.some_courier.presentation.ui_kit.shimmer.ShimmerType
import com.brsv44n.some_courier.presentation.ui_kit.shimmer.shimmerEffect
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.bgPrimary
import com.brsv44n.some_courier.theme.bgToast
import com.brsv44n.some_courier.theme.icon
import com.brsv44n.some_courier.theme.red
import com.brsv44n.some_courier.theme.strokeFocused
import com.brsv44n.some_courier.theme.strokesPrimary
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DrawerContent(
    modifier: Modifier = Modifier,
    drawerComponent: DrawerComponent,
) {
    val uiState by drawerComponent.uiState.subscribeAsState()

    val modalState by drawerComponent.modalState.subscribeAsState()

    when (modalState) {
        DrawerActiveModal.NONE -> {}

        DrawerActiveModal.LOGOUT -> {
            ConfirmationBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.title_confirmation_modal_logout),
                description = stringResource(R.string.label_confirmation_modal_logout),
                onConfirm = { drawerComponent.onEvent(Event.LogoutConfirmed) },
                onDeny = { drawerComponent.onEvent(Event.ModalDismissed) }
            )
        }
    }

    ModalDrawerSheet(
        modifier = modifier,
        drawerShape = RectangleShape,
        drawerContainerColor = bgPrimary
    ) {
        NamedRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 64.dp - WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                )
                .padding(horizontal = 28.dp),
            textModifier = Modifier
                .fillMaxWidth()
                .shimmerEffect(
                    enabled = uiState.isLoading || uiState.error != null,
                    type = ShimmerType.Horizontal,
                    cornerRadius = CornerRadius(8f, 8f)
                ),
            name = stringResource(R.string.label_drawer_restaurant),
            text = uiState.currentUser?.currentRestaurant.let {
                it ?: stringResource(R.string.label_not_selected_field)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 28.dp),
            thickness = 1.dp,
            color = strokesPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            shape = RectangleShape,
            label = {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = stringResource(R.string.label_drawer_orders),
                    style = TextStyles.AndroidBodyLarge,
                    color = bgToast
                )
            },
            selected = false,
            onClick = { drawerComponent.onEvent(Event.OrdersClicked) }
        )
        NavigationDrawerItem(
            shape = RectangleShape,
            label = {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = stringResource(R.string.label_drawer_history),
                    style = TextStyles.AndroidBodyLarge,
                    color = bgToast
                )
            },
            selected = false,
            onClick = { drawerComponent.onEvent(Event.HistoryClicked) }
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationDrawerItem(
            shape = RectangleShape,
            icon = {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_profile),
                    contentDescription = null,
                    tint = icon
                )
            },
            label = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shimmerEffect(
                            enabled = uiState.isLoading || uiState.error != null,
                            type = ShimmerType.Horizontal,
                            cornerRadius = CornerRadius(8f, 8f)
                        ),
                    text = uiState.currentUser?.name.orEmpty(),
                    style = TextStyles.AndroidBodyLarge,
                    color = bgToast
                )
            },
            selected = false,
            onClick = { drawerComponent.onEvent(Event.ProfileClicked) }
        )

        NavigationDrawerItem(
            shape = RectangleShape,
            icon = {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = null,
                    tint = icon
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.label_drawer_settings),
                    style = TextStyles.AndroidBodyLarge,
                    color = bgToast
                )
            },
            selected = false,
            onClick = { drawerComponent.onEvent(Event.SettingsClicked) }
        )

        NavigationDrawerItem(
            shape = RectangleShape,
            icon = {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_logout),
                    contentDescription = null,
                    tint = red
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.label_drawer_logout),
                    style = TextStyles.AndroidBodyLarge,
                    color = red
                )
            },
            selected = false,
            onClick = { drawerComponent.onEvent(Event.LogoutClicked) }
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 28.dp),
            thickness = 1.dp,
            color = strokesPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))
        DrawerFooter(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun DrawerFooter(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(
                id = R.string.label_drawer_app_version,
                BuildConfig.VERSION_NAME
            ),
            style = TextStyles.AndroidLabelMediumProminent,
            color = strokeFocused
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(
                id = R.string.label_drawer_developed
            ),
            style = TextStyles.AndroidLabelMediumProminent,
            color = strokeFocused
        )
    }
}

@Composable
@Preview
private fun PreviewDrawerContent() {
    CHPAndroidCourierTheme {
        DrawerContent(
            modifier = Modifier.fillMaxSize(),
            drawerComponent = PreviewDrawerComponent()
        )
    }
}
