package com.brsv44n.some_courier.location.google

import android.app.Activity
import com.brsv44n.some_courier.location.ResolvableException
import com.google.android.gms.common.api.ResolvableApiException

class GoogleResolvableException(
    private val exception: ResolvableApiException
) : ResolvableException() {

    override fun resolve(activity: Activity, requestCode: Int) {
        exception.startResolutionForResult(activity, requestCode)
    }
}
