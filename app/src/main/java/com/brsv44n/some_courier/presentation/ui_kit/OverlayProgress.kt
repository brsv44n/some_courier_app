package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlayProgress(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {}
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .size(48.dp)
        )
    }
}
