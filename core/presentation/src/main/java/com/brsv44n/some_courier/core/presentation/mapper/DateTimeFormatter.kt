package com.brsv44n.some_courier.core.presentation.mapper

import android.content.Context
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import java.util.Locale

class DateTimeFormatter(
    private val context: Context
) {

    fun format(date: LocalDate, pattern: String): String =
        date.toString(pattern, Locale("ru", "RU"))

    fun format(date: DateTime, pattern: String): String =
        date.toString(pattern, Locale("ru", "RU"))

    fun format(time: LocalTime, pattern: String): String =
        time.toString(pattern, Locale("ru", "RU"))

    fun format(date: LocalDate, patternRes: Int): String =
        date.toString(context.getString(patternRes), Locale("ru", "RU"))

    fun format(date: DateTime, patternRes: Int): String =
        date.toString(context.getString(patternRes), Locale("ru", "RU"))

    fun format(time: LocalTime, patternRes: Int): String =
        time.toString(context.getString(patternRes), Locale("ru", "RU"))
}
