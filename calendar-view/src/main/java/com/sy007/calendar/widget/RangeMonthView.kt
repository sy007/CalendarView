package com.sy007.calendar.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import com.sy007.calendar.*
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.entity.CalendarRangeSelected
import com.sy007.calendar.utils.Util

/**
 * 月份视图范围选择基类
 * @author sy007
 * @date 2022/4/13
 */
abstract class RangeMonthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseMonthView(context, attrs, defStyleAttr) {
    /**
     * 首次选中的日期
     */
    private val firstSelected: CalendarDay?
        get() = selected.firstSelected

    /**
     * 最后一次选中的日期
     */
    private val lastSelected: CalendarDay?
        get() = selected.lastSelected


    private val onDrawSelectedRangeBlock = { canvas: Canvas, _: CalendarDay, rect: Rect ->
        onDrawSelectedRange(canvas, rect)
    }

    /**
     * 设置最大可以选择的天数
     * <p>比如maximumDay=10 当选中第一个日期2019-9-3，那么第二个日期可以选择的日期范围为[2019-9-4,2019-9-12]
     */
    var maximumDay = -1

    /**
     * 设置日期点击回调
     */
    var onSelectedListener: OnRangeSelectedListener? = null

    /**
     * 设置选中的日期
     */
    var selected: CalendarRangeSelected = CalendarRangeSelected()


    override fun onInterceptSelect(calendarDay: CalendarDay): Boolean {
        return super.onInterceptSelect(calendarDay) || isOutOfMaximumRange(calendarDay)
    }

    /**
     * 是否是第一次选中的日期
     */
    fun isFirstSelected(calendarDay: CalendarDay) = calendarDay == firstSelected

    /**
     * 是否是最后一次选中的日期
     */
    fun isLastSelected(calendarDay: CalendarDay) = calendarDay == lastSelected

    override fun onClickCalendarDay(calendarDay: CalendarDay) {
        val listener = this.onSelectedListener ?: return
        val firstSelected = this.firstSelected
        val lastSelected = this.lastSelected
        if (firstSelected == null) {
            selected.firstSelected = calendarDay
        } else {
            if (lastSelected != null) {
                selected.firstSelected = calendarDay
                selected.lastSelected = null
            } else {
                when (calendarDay.compareTo(firstSelected)) {
                    0, -1 -> selected.firstSelected = calendarDay
                    1 -> selected.lastSelected = calendarDay
                }
            }
        }
        listener.onSelected(selected)
    }

    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect) {
        onDrawDay(canvas, calendarDay, rect, calendarDay == firstSelected || calendarDay == lastSelected)
    }


    override fun onDrawMonthAfter(canvas: Canvas, calendarDay: CalendarDay) {
        super.onDrawMonthAfter(canvas, calendarDay)
        drawSelectedRange(canvas, calendarDay)
    }

    /**
     * 绘制选中的范围
     *
     * <p>根据首次选中和最后一次选中计算出选中的范围，你可以根据rect范围中每个日子边界，来自定义绘制选中范围样式
     *
     * @param rect 选中范围中每个日子的绘制边界
     */
    protected abstract fun onDrawSelectedRange(canvas: Canvas, rect: Rect)

    private fun drawSelectedRange(canvas: Canvas, calendarDay: CalendarDay) {
        val firstSelected = this.firstSelected ?: return
        val lastSelected = this.lastSelected ?: return
        var start: Int
        var end: Int
        when {
            calendarDay.sameMonth(firstSelected) && calendarDay.sameMonth(lastSelected) -> {
                //选择同一月
                start = firstSelected.day + 1
                end = lastSelected.day - 1
                draw(canvas, calendarDay, start, end, onDrawSelectedRangeBlock)
            }
            else -> {
                //选择不同月
                //绘制第一次选择的月份 选中范围
                if (calendarDay.sameMonth(firstSelected)) {
                    start = firstSelected.day + 1
                    end = firstSelected.lastDayOfMonth
                    draw(canvas, calendarDay, start, end, onDrawSelectedRangeBlock)
                }
                //绘制最后一次选择的月份 选中范围
                if (calendarDay.sameMonth(lastSelected)) {
                    start = lastSelected.firstDayOfMonth
                    end = lastSelected.day - 1
                    draw(canvas, calendarDay, start, end, onDrawSelectedRangeBlock)
                }
                //绘制中间月份选中范围
                if (isDrawSelectedRangeOfMiddleStatus(calendarDay.year, calendarDay.month)) {
                    start = calendarDay.firstDayOfMonth
                    end = calendarDay.lastDayOfMonth
                    draw(canvas, calendarDay, start, end, onDrawSelectedRangeBlock)
                }
            }
        }
    }

    /**
     * 是否超过日历选择的最大范围
     */
    private fun isOutOfMaximumRange(calendarDay: CalendarDay): Boolean {
        if (firstSelected != null && lastSelected != null) {
            return false
        }
        val firstSelected = firstSelected?.takeIf {
            maximumDay > 0
        } ?: return false
        return Util.betweenDay(firstSelected.calendar, calendarDay.calendar) + 1 > maximumDay
    }

    private fun isDrawSelectedRangeOfMiddleStatus(year: Int, month: Int): Boolean {
        val totalMonth = year * 12 + month
        val firstTotalMonth = firstSelected!!.year * 12 + firstSelected!!.month
        val lastTotalMonth = lastSelected!!.year * 12 + lastSelected!!.month
        return totalMonth in (firstTotalMonth + 1) until lastTotalMonth
    }
}