package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.theme.bgPrimary
import com.brsv44n.some_courier.theme.icon
import com.brsv44n.some_courier.theme.serviceError
import com.brsv44n.some_courier.theme.strokeEnabled
import com.brsv44n.some_courier.theme.strokeFocused
import com.brsv44n.some_courier.theme.textFieldTextInput
import com.brsv44n.some_courier.theme.textLabel

@Immutable
object ChpdTextFieldDefaults {
    fun stroke(
        focusedStrokeWidth: Dp = 2.dp,
        unfocusedStrokeWidth: Dp = 1.dp
    ): ChpdTextFieldStroke {
        return ChpdTextFieldStroke(
            focusedStrokeWidth = focusedStrokeWidth,
            unfocusedStrokeWidth = unfocusedStrokeWidth
        )
    }

    fun colors(
        focusedTextColor: Color = textFieldTextInput,
        unfocusedTextColor: Color = textFieldTextInput,
        disabledTextColor: Color = textFieldTextInput,
        errorTextColor: Color = textFieldTextInput,
        focusedPlaceholderColor: Color = textLabel,
        errorPlaceholderColor: Color = textLabel,
        disabledPlaceholderColor: Color = textLabel,
        unfocusedPlaceholderColor: Color = textLabel,
        focusedContainerColor: Color = bgPrimary,
        errorContainerColor: Color = bgPrimary,
        disabledContainerColor: Color = bgPrimary,
        unfocusedContainerColor: Color = bgPrimary,
        focusedTrailingIconColor: Color = icon,
        errorTrailingIconColor: Color = icon,
        disabledTrailingIconColor: Color = icon,
        unfocusedTrailingIconColor: Color = icon,
        focusedLeadingIconColor: Color = icon,
        errorLeadingIconColor: Color = icon,
        disabledLeadingIconColor: Color = icon,
        unfocusedLeadingIconColor: Color = icon,
        focusedStrokeColor: Color = strokeFocused,
        unfocusedStrokeColor: Color = strokeEnabled,
        disabledStrokeColor: Color = strokeEnabled,
        errorStrokeColor: Color = serviceError
    ): ChpdTextFieldColors = ChpdTextFieldColors(
        focusedTextColor = focusedTextColor,
        unfocusedTextColor = unfocusedTextColor,
        disabledTextColor = disabledTextColor,
        errorTextColor = errorTextColor,
        focusedStrokeColor = focusedStrokeColor,
        unfocusedStrokeColor = unfocusedStrokeColor,
        disabledStrokeColor = disabledStrokeColor,
        errorStrokeColor = errorStrokeColor,
        focusedPlaceholderColor = focusedPlaceholderColor,
        errorPlaceholderColor = errorPlaceholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        unfocusedPlaceholderColor = unfocusedPlaceholderColor,
        focusedContainerColor = focusedContainerColor,
        errorContainerColor = errorContainerColor,
        disabledContainerColor = disabledContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        focusedTrailingIconColor = focusedTrailingIconColor,
        errorTrailingIconColor = errorTrailingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        focusedLeadingIconColor = focusedLeadingIconColor,
        errorLeadingIconColor = errorLeadingIconColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor
    )
}

@Immutable
class ChpdTextFieldStroke(
    val focusedStrokeWidth: Dp,
    val unfocusedStrokeWidth: Dp
) {
    @Stable
    fun strokeWidth(isFocused: Boolean, isError: Boolean): Dp =
        if (isFocused || isError) focusedStrokeWidth else unfocusedStrokeWidth
}

@Immutable
class ChpdTextFieldColors(
    val focusedTextColor: Color,
    val unfocusedTextColor: Color,
    val disabledTextColor: Color,
    val errorTextColor: Color,
    val focusedStrokeColor: Color,
    val unfocusedStrokeColor: Color,
    val disabledStrokeColor: Color,
    val errorStrokeColor: Color,
    val focusedPlaceholderColor: Color,
    val errorPlaceholderColor: Color,
    val disabledPlaceholderColor: Color,
    val unfocusedPlaceholderColor: Color,
    val focusedContainerColor: Color,
    val errorContainerColor: Color,
    val disabledContainerColor: Color,
    val unfocusedContainerColor: Color,
    val focusedTrailingIconColor: Color,
    val errorTrailingIconColor: Color,
    val disabledTrailingIconColor: Color,
    val unfocusedTrailingIconColor: Color,
    val focusedLeadingIconColor: Color,
    val errorLeadingIconColor: Color,
    val disabledLeadingIconColor: Color,
    val unfocusedLeadingIconColor: Color
) {

    @Stable
    fun strokeColor(
        isEnabled: Boolean,
        isError: Boolean,
        isFocused: Boolean,
    ): Color =
        when {
            !isEnabled -> disabledStrokeColor
            isError -> errorStrokeColor
            isFocused -> focusedStrokeColor
            else -> unfocusedStrokeColor
        }
}
