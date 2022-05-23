package com.sy007.calendar

import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.entity.CalendarRangeSelected

/**
 * @author sy007
 * @date 2019/01/20
 */
/**
 * 单选事件
 */
interface OnSelectedListener {
    fun onSelected(selected: CalendarDay)
}

/**
 * 范围选择事件
 */
interface OnRangeSelectedListener {
    /**
     * [CalendarRangeSelected.firstSelected]为第一次选择的日期，
     * [CalendarRangeSelected.lastSelected]为最后一次选择的日期。
     */
    fun onSelected(selected: CalendarRangeSelected)
}

/**
 * 多选选择事件
 */
interface OnMultipleSelectedListener {
    fun onSelected(selected: Set<CalendarDay>)
}

/**
 * 月份滚动事件
 */
interface OnMonthScrollListener {
    fun onScroll(calendarDay: CalendarDay)
}

internal interface OnCalendarDayClickListener {
    fun onClick(calendarDay: CalendarDay)
}

