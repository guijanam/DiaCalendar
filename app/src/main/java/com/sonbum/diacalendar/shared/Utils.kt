package com.sonbum.diacalendar.shared

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

fun String.makeDate(): Date {
    val formatter = SimpleDateFormat("yyyy/MM/dd")
    return formatter.parse(this)
}

fun Date.getDateString(): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd")
    return formatter.format(this)
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.year}.${this.month.displayText(short = short)}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.KOREAN)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.KOREAN).let { value ->
        if (uppercase) value.uppercase(Locale.KOREAN) else value
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }
        firstDate.year == lastDate.year -> {
            "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
        }
        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}


fun <T> Iterable<T>.splitAt(n: Int): Pair<List<T>, List<T>> =
    when {
        n < 0 ->
            throw IllegalArgumentException(
                "Requested split at index $n is less than zero.")

        n == 0 ->
            emptyList<T>() to toList()

        this is Collection<T> && (n >= size) ->
            toList() to emptyList()

        else -> {
            var idx = 0
            val dn = if (this is Collection<T>) size - n else n
            val left = ArrayList<T>(n)
            val right = ArrayList<T>(dn)

            @Suppress("UseWithIndex")
            for (item in this) {
                when (idx++ >= n) {
                    false -> left.add(item)
                    true -> right.add(item)
                }
            }
            left to right
        }
    }.let {
        it.first.optimizeReadOnlyList2() to it.second.optimizeReadOnlyList2()
    }

/**
 * Original function is internal in the stdlib
 */
fun <T> List<T>.optimizeReadOnlyList2(): List<T> = when (size) {
    0 -> emptyList()
    1 -> listOf(this[0])
    else -> this
}