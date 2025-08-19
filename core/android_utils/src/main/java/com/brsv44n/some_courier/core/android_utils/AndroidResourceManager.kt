package com.brsv44n.some_courier.core.android_utils

import android.content.Context
import com.brsv44n.some_courier.core.utils.ResourceManager

class AndroidResourceManager(
    private val context: Context
) : ResourceManager {

    override fun getString(resId: Int, vararg args: Any): String =
        context.getString(resId, *args)

    override fun getQuantityString(resId: Int, quantity: Int, vararg args: Any): String =
        context.resources.getQuantityString(resId, quantity, *args)

    override fun getStringArray(resId: Int): Array<String> =
        context.resources.getStringArray(resId)
}
