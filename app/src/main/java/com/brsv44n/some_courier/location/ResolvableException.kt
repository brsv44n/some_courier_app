package com.brsv44n.some_courier.location

import android.app.Activity

abstract class ResolvableException : Exception() {

    abstract fun resolve(activity: Activity, requestCode: Int)
}
