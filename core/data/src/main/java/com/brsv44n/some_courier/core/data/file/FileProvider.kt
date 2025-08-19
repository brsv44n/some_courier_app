package com.brsv44n.some_courier.core.data.file

interface FileProvider {

    fun createCompressedImageFile(originalPath: String): String

    fun deleteCompressedImageFile(path: String)
}
