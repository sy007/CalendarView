package com.sy007.calendar.simple.single

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.widget.SingleMonthView

/**
 * Created by sy007 on 2021/8/17.
 */
class SingleMonthViewSimple33 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SingleMonthView(context, attrs, defStyleAttr) {

    /**
     * 是否拦截日期选中事件，如果拦截则不会回调日期选中事件
     * 你可以在此方法中拦截不想让用户点击的日期
     * @return true 表示拦截，false 表示不拦截
     */
    override fun onInterceptSelect(calendarDay: CalendarDay): Boolean {
        return super.onInterceptSelect(calendarDay)
    }

    /**
     * 月份视图绘制前调用，你可以在绘制前做一些事
     */
    override fun onDrawMonthBefore(canvas: Canvas, calendarDay: CalendarDay) {
        super.onDrawMonthBefore(canvas, calendarDay)

    }

    /**
     * 月份视图绘制后调用，你可以在绘制后做一些事
     */
    override fun onDrawMonthAfter(canvas: Canvas, calendarDay: CalendarDay) {
        super.onDrawMonthAfter(canvas, calendarDay)
    }

    /**
     * 日期绘制时调用
     *
     * [calendarDay] 待绘制的日期模型
     * [rect]       日期绘制的范围
     * [isSelected] 该日期是否是选中状态
     */
    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect, isSelected: Boolean) {

    }
}