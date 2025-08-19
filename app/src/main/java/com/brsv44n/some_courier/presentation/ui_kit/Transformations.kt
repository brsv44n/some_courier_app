package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

private class PhoneOffsetMapper : OffsetMapping {

    private val transformedIndexes = listOf(4, 5, 6, 9, 10, 11, 13, 14, 16, 17, 18)
    private val originalIndexes = listOf(0, 0, 0, 0, 0, 1, 2, 3, 3, 3, 4, 5, 6, 6, 7, 8, 8, 9, 10)

    override fun originalToTransformed(offset: Int): Int {
        return transformedIndexes[offset]
    }

    override fun transformedToOriginal(offset: Int): Int {
        return originalIndexes[offset]
    }
}

fun transformPhoneInput(text: AnnotatedString): TransformedText {
    val phonePrefix = "+7"
    val trimmed = text.trim()
    if (trimmed.isBlank()) {
        return TransformedText(
            AnnotatedString("$phonePrefix "),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return 3
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return 0
                }
            }
        )
    }
    var result = phonePrefix
    text.forEachIndexed { index, char ->
        if (index == 0) result += " ("
        result += char
        if (index == 2) result += ") "
        if (index == 5 || index == 7) result += "-"
    }
    val offsetTranslator = PhoneOffsetMapper()
    return TransformedText(AnnotatedString(result), offsetTranslator)
}
