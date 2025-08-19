package com.brsv44n.some_courier.location

import com.brsv44n.some_courier.core.domain.entities.PointEntity
import kotlinx.coroutines.flow.Flow

interface CoroutineLocationProvider {

    fun locationFlow(interval: Long): Flow<PointEntity>
}
