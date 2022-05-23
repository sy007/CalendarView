package com.sy007.calendar

import android.view.View
import android.view.ViewGroup
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.widget.BaseMonthView

/**
 * @author sy007
 * @date 2022/5/1
 */
interface ViewBinder<T : View> {
    fun create(parent: ViewGroup): T
    fun onBind(view: T, calendarDay: CalendarDay)
}

interface MonthViewBinder<T : BaseMonthView> : ViewBinder<T>

abstract class MonthHeaderViewBinder<T : View> : ViewBinder<T> {
    /**
     * 是否悬停 true 悬停，false 不悬停
     */
    open fun isStick(): Boolean {
        return false
    }
}