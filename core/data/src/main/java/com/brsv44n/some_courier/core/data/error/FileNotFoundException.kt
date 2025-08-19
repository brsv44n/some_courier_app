package com.brsv44n.some_courier.core.data.error

class FileNotFoundException(path: String) :
    IllegalArgumentException("File not found at path: $path")
