package com.brsv44n.some_courier.gradleconfig

enum class ServerFlavorConfig(
    val flavorName: String,
    val applicationIdSuffix: String?,
    val versionNameSuffix: String?,
    val baseUrl: String,
    val sentryDsn: String,
    val appMetricaKey: String,
    val proguardFile: String? = null
) {

    TEST(
        flavorName = "clientTest",
        applicationIdSuffix = ".test",
        versionNameSuffix = "-test",
        baseUrl = "",
        sentryDsn = "",
        appMetricaKey = "todo"
    ),
    PRODUCTION(
        flavorName = "clientProduction",
        applicationIdSuffix = null,
        versionNameSuffix = null,
        baseUrl = "",
        sentryDsn = "",
        appMetricaKey = "todo",
        proguardFile = null
    )
}
