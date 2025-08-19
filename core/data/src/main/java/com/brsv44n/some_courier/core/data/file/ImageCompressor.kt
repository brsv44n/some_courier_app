package com.brsv44n.some_courier.core.data.file

interface ImageCompressor {

    fun compressImage(
        inputFilePath: String,
        outputFilePath: String,
        reqWidth: Int,
        reqHeight: Int,
        quality: Int
    )
}
