@file:JvmName("CalendarDayUtil")

package com.sy007.calendar

import android.annotation.SuppressLint
import com.sy007.calendar.entity.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author sy007
 * @date 2019/01/20
 */

/**
 * 是否在指定日期之前
 */
fun CalendarDay.before(calendarDay: CalendarDay): Boolean {
    if (year < calendarDay.year) {
        return true
    } else if (year > calendarDay.year) {
        return false
    }
    if (month < calendarDay.month) {
        return true
    } else if (month > calendarDay.month) {
        return false
    }
    return day < calendarDay.day
}


/**
 * 是否在指定日期之后
 */
fun CalendarDay.after(calendarDay: CalendarDay): Boolean {
    if (year > calendarDay.year) {
        return true
    } else if (year < calendarDay.year) {
        return false
    }
    if (month > calendarDay.month) {
        return true
    } else if (month < calendarDay.month) {
        return false
    }
    return day > calendarDay.day
}

/**
 * 是否是今天
 */
val CalendarDay.isToday: Boolean get() = year == curYear && month == curMonth && day == curDay

/**
 * 月份的最后一天
 */
val CalendarDay.lastDayOfMonth: Int
    get() {
        val calendar = calendar
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

/**
 * 月份第一天
 */
val CalendarDay.firstDayOfMonth: Int
    get() {
        val calendar = calendar
        return calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
    }

/**
 * 日期是否过期(与当前日期比较)
 */
val CalendarDay.isOverDue: Boolean
    get() {
        return before(CalendarDay(curCalendar))
    }

/**
 * 是否是周末
 */
val CalendarDay.isWeekend: Boolean
    get() {
        val calendar = calendar
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

/**
 * CalendarDay转换为date
 */
val CalendarDay.date: Date get() = calendar.time


/**
 * 格式化日期
 */
@SuppressLint("SimpleDateFormat")
fun CalendarDay.formatDate(format: String): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(date)
}

/**
 * 两个日期月份是否相等
 */
fun CalendarDay.sameMonth(calendarDay: CalendarDay?): Boolean {
    calendarDay ?: return false
    return year == calendarDay.year && month == calendarDay.month
}

/**
 * 两个日期是否相等
 */
fun CalendarDay.sameDay(calendarDay: CalendarDay?): Boolean {
    calendarDay ?: return false
    return sameMonth(calendarDay) && day == calendarDay.day
}

/**
 * 根据当前日期创建前一个月的日期
 */
internal fun CalendarDay.createPreCalendarDay(): CalendarDay {
    val preCalendar = calendar
    preCalendar.add(Calendar.MONTH, -1)
    preCalendar[Calendar.DAY_OF_MONTH] = 1
    val preCalendarDay = CalendarDay(preCalendar)
    preCalendarDay.dayOwner = CalendarDay.DayOwner.PRE_MONTH
    return preCalendarDay
}

/**
 * 根据当前日期创建后一个月的日期
 */
internal fun CalendarDay.createNextCalendarDay(): CalendarDay {
    val preCalendar = calendar
    preCalendar.add(Calendar.MONTH, 1)
    preCalendar[Calendar.DAY_OF_MONTH] = 1
    val preCalendarDay = CalendarDay(preCalendar)
    preCalendarDay.dayOwner = CalendarDay.DayOwner.NEXT_MONTH
    return preCalendarDay
}

