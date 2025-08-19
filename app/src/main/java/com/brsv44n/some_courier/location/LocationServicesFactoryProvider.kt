package com.brsv44n.some_courier.location

import android.content.Context
import com.brsv44n.some_courier.location.android.AndroidLocationServicesFactory
import com.brsv44n.some_courier.location.google.GoogleLocationServicesFactory
import com.brsv44n.some_courier.location.huawei.HuaweiLocationServicesFactory
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import me.tatarka.inject.annotations.Inject

@Inject
class LocationServicesFactoryProvider(
    private val context: Context
) {

    fun getFactory(): LocationServicesAbstractFactory {
        return when {
            isGmsAvailable() -> GoogleLocationServicesFactory(context)
            isHmsAvailable() -> HuaweiLocationServicesFactory(context)
            else -> AndroidLocationServicesFactory(context)
        }
    }

    private fun isGmsAvailable(): Boolean {
        val availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        return availability == ConnectionResult.SUCCESS
    }

    private fun isHmsAvailable(): Boolean {
        return HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(context) == com.huawei.hms.api.ConnectionResult.SUCCESS
    }
}
