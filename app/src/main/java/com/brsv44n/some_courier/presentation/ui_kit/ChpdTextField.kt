package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.theme.TextStyles

@Composable
fun ChpdTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyles.AndroidBodyLarge,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(12.dp),
    colors: ChpdTextFieldColors = ChpdTextFieldDefaults.colors(),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedTextColor = colors.focusedTextColor,
            unfocusedTextColor = colors.unfocusedTextColor,
            disabledTextColor = colors.disabledTextColor,
            errorTextColor = colors.errorTextColor,
            focusedPlaceholderColor = colors.focusedPlaceholderColor,
            errorPlaceholderColor = colors.errorPlaceholderColor,
            disabledPlaceholderColor = colors.disabledPlaceholderColor,
            unfocusedPlaceholderColor = colors.unfocusedPlaceholderColor,
            focusedContainerColor = colors.focusedContainerColor,
            errorContainerColor = colors.errorContainerColor,
            disabledContainerColor = colors.disabledContainerColor,
            unfocusedContainerColor = colors.unfocusedContainerColor,
            focusedIndicatorColor = colors.unfocusedStrokeColor,
            errorIndicatorColor = colors.errorStrokeColor,
            disabledIndicatorColor = colors.unfocusedStrokeColor,
            unfocusedIndicatorColor = colors.unfocusedStrokeColor,
            focusedTrailingIconColor = colors.focusedTrailingIconColor,
            errorTrailingIconColor = colors.errorTrailingIconColor,
            disabledTrailingIconColor = colors.disabledTrailingIconColor,
            unfocusedTrailingIconColor = colors.unfocusedTrailingIconColor,
            focusedLeadingIconColor = colors.focusedLeadingIconColor,
            errorLeadingIconColor = colors.errorLeadingIconColor,
            disabledLeadingIconColor = colors.disabledLeadingIconColor,
            unfocusedLeadingIconColor = colors.unfocusedLeadingIconColor
        )
    )
}
