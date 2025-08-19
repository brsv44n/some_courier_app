package com.brsv44n.some_courier.core.utils

import com.brsv44n.some_courier.core.domain.entities.PointEntity

interface ExternalAppNavigator {

    fun openDialApp(phone: String, newTask: Boolean = true)

    fun openUrl(url: String, newTask: Boolean = true)

    fun openEmail(email: String, newTask: Boolean = true)

    fun openRouteSinglePoint(point: PointEntity, newTask: Boolean = true)

    fun openRouteMultiplePoints(points: List<PointEntity?>, newTask: Boolean = true)

    fun openLocationSettings()

    fun openAppSettings()
}
