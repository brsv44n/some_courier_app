package com.brsv44n.some_courier.location.huawei

import android.app.Activity
import com.brsv44n.some_courier.location.ResolvableException
import com.huawei.hms.common.ResolvableApiException

class HuaweiResolvableException(
    private val exception: ResolvableApiException
) : ResolvableException() {

    override fun resolve(activity: Activity, requestCode: Int) {
        exception.startResolutionForResult(activity, requestCode)
    }
}
