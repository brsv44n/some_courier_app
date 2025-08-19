package com.brsv44n.some_courier.core.network

import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.BuildInfo

class HttpClientDependencies(
    val authHolder: AuthHolder,
    val networkStateChecker: NetworkStateChecker,
    // val networkErrorLogger: NetworkErrorLogger,
    // val imageMultiplierProvider: ImageMultiplierProvider,
    val buildInfo: BuildInfo
)
