package com.brsv44n.some_courier.core.utils

sealed interface Text {
    class Resource(val resId: Int, vararg val args: Any) : Text {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Resource

            if (resId != other.resId) return false
            return args.contentEquals(other.args)
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    class Simple(val value: String) : Text {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Simple

            return value == other.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }
    }

    data object Empty : Text
}
