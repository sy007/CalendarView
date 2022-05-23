package com.sy007.calendar.entity

import com.sy007.calendar.before
import java.io.Serializable
import java.util.*

/**
 * 日期模型类
 * @author sy007
 * @date 2019/01/20
 */
class CalendarDay : Serializable, Comparable<CalendarDay> {

    private val tempCalendar = Calendar.getInstance()

    /**
     * 当前绘制的年份
     */
    var year: Int

    /**
     * 当前绘制的月份，从0-11
     */
    var month: Int

    /**
     * 当前绘制的天，从1-31
     */
    var day: Int

    /**
     * 当前时间年份
     */
    val curYear: Int = tempCalendar[Calendar.YEAR]

    /**
     * 当前时间月份，从0-11
     */
    val curMonth: Int = tempCalendar[Calendar.MONTH]

    /**
     * 当前时间的日子，从1-31
     */
    val curDay: Int = tempCalendar[Calendar.DAY_OF_MONTH]

    /**
     * 当前日期属于哪一月，默认为当前月
     */
    var dayOwner = DayOwner.THIS_MONTH


    /**
     * 当前日期属于哪一月
     */
    enum class DayOwner {
        /**
         * 当前月的前一月
         */
        PRE_MONTH,

        /**
         * 当前月
         */
        THIS_MONTH,

        /**
         * 当前月的后一月
         */
        NEXT_MONTH
    }


    constructor(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    constructor(calendar: Calendar) : this(calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])


    /**
     * 当前日期的Calendar
     */
    val calendar: Calendar
        get() {
            tempCalendar.clear()
            tempCalendar[Calendar.YEAR] = year
            tempCalendar[Calendar.MONTH] = month
            tempCalendar[Calendar.DAY_OF_MONTH] = day
            return tempCalendar
        }

    /**
     * 当前时间的Calendar
     */
    val curCalendar: Calendar
        get() {
            tempCalendar.clear()
            tempCalendar[Calendar.YEAR] = curYear
            tempCalendar[Calendar.MONTH] = curMonth
            tempCalendar[Calendar.DAY_OF_MONTH] = curDay
            return tempCalendar
        }

    override fun toString(): String {
        return "CalendarDay{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}'
    }


    override fun compareTo(other: CalendarDay): Int {
        if (equals(other)) {
            return 0
        }
        return if (before(other)) -1 else 1
    }

    override fun equals(other: Any?): Boolean {
        val that = other as? CalendarDay ?: return false
        if (year != that.year) return false
        if (month != that.month) return false
        if (day != that.day) return false
        if (dayOwner != that.dayOwner) return false
        return true
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + day
        result = 31 * result + dayOwner.hashCode()
        return result
    }
}