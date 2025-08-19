package com.brsv44n.some_courier.core.data.file

interface FileDataSource {

    fun getFileFromTempPath(path: String): File

    fun getFileFromPath(path: String): File
}
