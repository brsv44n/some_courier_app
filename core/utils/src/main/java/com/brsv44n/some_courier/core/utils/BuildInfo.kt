package com.brsv44n.some_courier.core.utils

open class BuildInfo(
    open val platformName: String,
    open val appName: String,
    open val appIcon: Int,
    open val versionName: String,
    open val versionCode: Int
) {
    val appVersion: String
        get() = versionCode.toString()
}
