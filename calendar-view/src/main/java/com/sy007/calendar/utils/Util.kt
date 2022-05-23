package com.sy007.calendar.utils

import android.content.Context
import java.util.*

/**
 * @author sy007
 * @date 2019/1/20
 */
object Util {
    fun dip2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    fun sp2px(context: Context, spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    /**
     * 获取两个日期月数差
     *
     * @param start
     * @param end
     * @return
     */
    fun diffMonth(start: Calendar, end: Calendar): Int {
        val yearEnd = end[Calendar.YEAR]
        val yearStart = start[Calendar.YEAR]
        val monthEnd = end[Calendar.MONTH]
        val monthStart = start[Calendar.MONTH]
        if (yearStart == yearEnd && monthStart == monthEnd) {
            return 0
        }
        var yearInterval = yearEnd - yearStart
        if (monthEnd < monthStart) {
            yearInterval--
        }
        // 获取月数差值
        var monthInterval = monthEnd + 12 - monthStart
        monthInterval %= 12
        return yearInterval * 12 + monthInterval
    }

    /**
     * 获取两个日期天数差
     */
    fun betweenDay(c1: Calendar, c2: Calendar): Long {
        var c1 = c1
        if (c1.after(c2)) {
            val c1Time = c1.time
            c1 = c2
            c2.time = c1Time
        }
        return (c2.timeInMillis - c1.timeInMillis) / (1000 * 3600 * 24)
    }
}