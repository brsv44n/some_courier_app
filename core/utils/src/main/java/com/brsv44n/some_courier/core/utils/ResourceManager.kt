package com.brsv44n.some_courier.core.utils

interface ResourceManager {

    fun getString(resId: Int, vararg args: Any): String

    fun getQuantityString(resId: Int, quantity: Int, vararg args: Any): String

    fun getStringArray(resId: Int): Array<String>
}
