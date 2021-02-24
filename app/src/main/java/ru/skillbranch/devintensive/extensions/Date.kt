package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.shortFormat(): String {
    val pattern = if(this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time/ DAY
    val day2 = date.time/ DAY
    return day1 == day2
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    return when (val diff = max(date.time, this.time) - min(date.time, this.time)) {
        in 0..1 * SECOND -> "только что"
        in 1 * SECOND..45 * SECOND -> "несколько секунд назад"
        in 45 * SECOND..75 * SECOND -> "минуту назад"
        in 75 * SECOND..(45 * MINUTE) -> "${TimeUnits.MINUTE.plural((diff / MINUTE).toInt())} назад"
        in 45 * MINUTE..75 * MINUTE -> "час назад"
        in 75 * MINUTE..22 * HOUR -> "${TimeUnits.HOUR.plural((diff / HOUR).toInt())} назад"
        in 22 * HOUR..26 * HOUR -> "день назад"
        in 26 * HOUR..360 * DAY -> "${TimeUnits.DAY.plural((diff / DAY).toInt())} назад"
        else -> "более года назад"
    }
}

enum class TimeUnits {
    SECOND {
        override fun plural(value: Int): String = when {
            value % 10 == 1 && value % 100 != 11 -> "$value секунду"
            value % 10 in 2..4 && value % 100 !in 12..14 -> "$value секунды"
            else -> "$value секунд"
        }
    },
    MINUTE {
        override fun plural(value: Int): String = when {
            value % 10 == 1 && value % 100 != 11 -> "$value минуту"
            value % 10 in 2..4 && value % 100 !in 12..14 -> "$value минуты"
            else -> "$value минут"
        }
    },
    HOUR {
        override fun plural(value: Int): String = when {
            value % 10 == 1 && value % 100 != 11 -> "$value час"
            value % 10 in 2..4 && value % 100 !in 12..14 -> "$value часа"
            else -> "$value часов"
        }
    },
    DAY {
        override fun plural(value: Int): String = when {
            value % 10 == 1 && value % 100 != 11 -> "$value день"
            value % 10 in 2..4 && value % 100 !in 12..14 -> "$value дня"
            else -> "$value дней"
        }
    };

    abstract fun plural(value: Int): String
}