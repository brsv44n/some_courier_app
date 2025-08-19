package com.brsv44n.some_courier.core.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ImageDto(@SerialName("mobile") val mobile: MobileImageDto)
