package com.sy007.calendar.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.OnMultipleSelectedListener

/**
 * 月份视图多选基类
 * @author sy007
 * @date 2022/4/13
 */
abstract class MultipleMonthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseMonthView(context, attrs, defStyleAttr) {
    /**
     * 设置日期点击回调
     */
    var onMultipleSelectedListener: OnMultipleSelectedListener? = null

    /**
     * 设置选中的日期
     */
    var selected: MutableSet<CalendarDay> = mutableSetOf()

    override fun onClickCalendarDay(calendarDay: CalendarDay) {
        if (selected.contains(calendarDay)) {
            selected.remove(calendarDay)
        } else {
            selected.add(calendarDay)
        }
        onMultipleSelectedListener?.onSelected(selected)
    }

    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect) {
        onDrawDay(canvas, calendarDay, rect, selected.contains(calendarDay))
    }
}