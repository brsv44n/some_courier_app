package com.brsv44n.some_courier.core.presentation

import com.brsv44n.some_courier.core.domain.entities.Money
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Money.formatCurrency(): String = roubles.formatCurrency()
fun Money.formatCurrencyToRoubles(): String = roubles.formatCurrencyToRoubles()

fun Double.formatCurrency(): String {
    return if (this - this.toLong() == 0.0) this.formatPriceWithoutKopecks()
    else this.formatPriceWithKopecks()
}

fun Double.formatCurrencyToRoubles(): String {
    return formatCurrency() + "\u00a0\u20bd"
}

private fun Double.formatPriceWithoutKopecks(): String {
    val dfs = DecimalFormatSymbols.getInstance().apply {
        groupingSeparator = '\u00a0'
        decimalSeparator = ','
    }
    val nf = DecimalFormat("#,##0", dfs).apply {
        isGroupingUsed = true
    }
    return nf.format(this)
}

private fun Double.formatPriceWithKopecks(): String {
    val dfs = DecimalFormatSymbols.getInstance().apply {
        groupingSeparator = '\u00a0'
        decimalSeparator = ','
    }
    val nf = DecimalFormat("#,##0.00", dfs).apply {
        isGroupingUsed = true
    }
    return nf.format(this)
}
