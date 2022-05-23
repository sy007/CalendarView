package com.sy007.calendar.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.OnSelectedListener

/**
 * 月份视图单选基类
 * @author sy007
 * @date 2022/4/13
 */
abstract class SingleMonthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseMonthView(context, attrs, defStyleAttr) {

    /**
     * 设置日期点击回调
     */
    var onSelectedListener: OnSelectedListener? = null

    /**
     * 设置选中的日期
     */
    var selected: CalendarDay? = null

    override fun onClickCalendarDay(calendarDay: CalendarDay) {
        onSelectedListener?.onSelected(calendarDay)
    }

    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect) {
        onDrawDay(canvas, calendarDay, rect, calendarDay == selected)
    }

}