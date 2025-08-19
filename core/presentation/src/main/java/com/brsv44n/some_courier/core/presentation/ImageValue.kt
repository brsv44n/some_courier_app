package com.brsv44n.some_courier.core.presentation

import com.brsv44n.some_courier.core.domain.ImageVariant

sealed class ImageValue {
    data class Resource(val drawableRes: Int) : ImageValue()
    data class Variants(val variants: List<ImageVariant>) : ImageValue() {
        constructor(variant: ImageVariant) : this(listOf(variant))
    }
}
