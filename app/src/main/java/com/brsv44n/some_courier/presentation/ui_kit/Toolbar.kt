package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.onSurface
import com.brsv44n.some_courier.theme.textBody
import com.brsv44n.some_courier.theme.textTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationToolbar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    backgroundColor: Color = Color.White,
    contentColor: Color = textTitle,
    actionsColor: Color = textBody,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = contentColor,
            navigationIconContentColor = actionsColor,
            actionIconContentColor = actionsColor
        ),
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBackToolbar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    backgroundColor: Color = Color.White,
    contentColor: Color = textTitle,
    actionsColor: Color = textBody,
    actions: @Composable RowScope.() -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = contentColor,
            navigationIconContentColor = actionsColor,
            actionIconContentColor = actionsColor
        ),
        actions = actions
    )
}

@Composable
@Preview
private fun PreviewToolbar() {
    CHPAndroidCourierTheme {
        NavigationBackToolbar()
    }
}
