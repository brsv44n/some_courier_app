package com.brsv44n.some_courier.core.domain.entities

class Money(
    val kopecks: Long
) : Comparable<Money> {

    companion object {
        val zero = Money(0)
    }

    val roubles get() = kopecks.div(100.0)

    override operator fun compareTo(other: Money): Int =
        when {
            kopecks > other.kopecks -> 1
            kopecks < other.kopecks -> -1
            else -> 0
        }

    override fun equals(other: Any?): Boolean {
        if (other !is Money) return false
        return other.kopecks == kopecks
    }

    override fun hashCode(): Int {
        return kopecks.hashCode()
    }

    override fun toString(): String {
        return "Money(kopecks=$kopecks)"
    }

    operator fun plus(money: Money): Money = Money(kopecks + money.kopecks)

    operator fun minus(money: Money): Money = Money(kopecks - money.kopecks)

    operator fun times(times: Int): Money = Money(kopecks * times)

    operator fun times(times: Long): Money = Money(kopecks * times)

    operator fun div(div: Int): Money = Money(kopecks / div)

    operator fun div(div: Long): Money = Money(kopecks / div)
}
