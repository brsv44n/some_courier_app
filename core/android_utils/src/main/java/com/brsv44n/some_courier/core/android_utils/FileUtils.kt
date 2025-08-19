package com.brsv44n.some_courier.core.android_utils

import android.content.Context
import java.io.File

fun createCacheFile(
    context: Context,
    prefix: String,
    suffix: String
): File {
    val dir = context.cacheDir
    return File.createTempFile(prefix, suffix, dir)
}
