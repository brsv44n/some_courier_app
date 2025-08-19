package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.dark
import com.brsv44n.some_courier.core.presentation.models.ErrorModel

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    errorType: ErrorModel,
) {
    val title: String
    val label: String
    val image: Painter

    when (errorType) {
        ErrorModel.NoInternet -> {
            title = stringResource(R.string.title_error_screen_no_internet)
            label = stringResource(R.string.label_error_screen_no_internet)
            image = painterResource(R.drawable.ic_internet_error)
        }

        ErrorModel.Unknown -> {
            title = stringResource(R.string.title_error_screen_unknown)
            label = stringResource(R.string.label_error_screen_unknown)
            image = painterResource(R.drawable.ic_unknown_error)
        }
    }
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = image,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                color = dark,
                style = TextStyles.AndroidTitleLarge,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = label,
                color = dark,
                style = TextStyles.AndroidBodyLarge,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(24.dp))

        }
        BottomBlock(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            ChpdPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                onClick = onClick,
                contentPadding = PaddingValues(vertical = 28.dp)
            ) {
                Text(
                    text = stringResource(R.string.action_error_screen_reload)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewInternetErrorScreen() {
    CHPAndroidCourierTheme {
        ErrorScreen(
            modifier = Modifier.fillMaxSize(),
            onClick = {},
            errorType = ErrorModel.Unknown
        )
    }
}
