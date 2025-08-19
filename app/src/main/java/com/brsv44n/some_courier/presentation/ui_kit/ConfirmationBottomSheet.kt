package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.bgPrimary
import com.brsv44n.some_courier.theme.dark
import com.brsv44n.some_courier.theme.outline
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConfirmationBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    title: String,
    description: String,
    confirmButtonText: String = stringResource(R.string.action_confirm),
    denyButtonText: String = stringResource(R.string.action_dismiss),
    onConfirm: () -> Unit = {},
    onDeny: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val hideModalBottomSheet: (() -> Unit) -> Unit = { lambda ->
        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { lambda.invoke() }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDeny,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = bgPrimary,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = outline)
        }
    ) {
        ConfirmationBottomSheetContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            title = title,
            description = description,
            confirmButtonText = confirmButtonText,
            denyButtonText = denyButtonText,
            onDeny = {
                hideModalBottomSheet(onDeny)
            },
            onConfirm = {
                hideModalBottomSheet(onConfirm)
            }
        )
    }
}

@Composable
private fun ConfirmationBottomSheetContent(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    confirmButtonText: String = stringResource(R.string.action_confirm),
    denyButtonText: String = stringResource(R.string.action_dismiss),
    onDeny: () -> Unit,
    onConfirm: () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = TextStyles.AndroidTitleLarge,
            color = dark
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = description,
            style = TextStyles.AndroidBodyLarge,
            color = dark
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ChpdOutlinedButton(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 28.dp),
                onClick = onDeny,
                content = { Text(text = denyButtonText) }
            )
            Spacer(modifier = Modifier.width(12.dp))
            ChpdPrimaryButton(
                modifier = Modifier.weight(1f),
                onClick = onConfirm,
                contentPadding = PaddingValues(vertical = 28.dp),
                content = { Text(confirmButtonText) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun ConfirmationBottomSheetPreview() {
    CHPAndroidCourierTheme {
        ConfirmationBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.title_confirmation_modal_problem),
            description = stringResource(R.string.label_confirmation_modal_problem),
            onConfirm = {},
        )
    }
}
