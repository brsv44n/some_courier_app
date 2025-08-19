package com.brsv44n.some_courier.core.android_utils

import com.brsv44n.some_courier.core.utils.BuildInfo

data class AndroidBuildInfo(
    override val appName: String,
    override val appIcon: Int,
    override val versionName: String,
    override val versionCode: Int,
    val applicationId: String,
    val fileProviderAuthority: String = "$applicationId.provider"
) : BuildInfo(
    platformName = "Android",
    appName = appName,
    appIcon = appIcon,
    versionName = versionName,
    versionCode = versionCode
)
