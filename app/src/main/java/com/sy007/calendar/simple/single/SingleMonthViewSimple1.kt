package com.sy007.calendar.simple.single

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.sy007.calendar.*
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.widget.SingleMonthView
import com.sy007.calendar.utils.Util
import java.util.*
import com.sy007.calendar.simple.R

/**
 * Created by sy007 on 2021/8/17.
 */
class SingleMonthViewSimple1 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SingleMonthView(context, attrs, defStyleAttr) {

    private val selectTextColor = Color.parseColor("#FFFFFF")
    private val disClickTextColor = Color.parseColor("#BBBBBB")
    private val defaultTextColor = Color.parseColor("#000000")
    private val weekendTextColor = Color.parseColor("#FF6E00")
    private lateinit var dayTextPaint: Paint
    private lateinit var selectedBgDrawable: Drawable
    private lateinit var fm: Paint.FontMetrics
    var disableCalendarDays: Set<CalendarDay>? = null

    init {
        initPaint()
        initDrawable()
    }

    override fun onInterceptSelect(calendarDay: CalendarDay): Boolean {
        return super.onInterceptSelect(calendarDay)
                || isInterceptSelect(calendarDay)
    }

    /**
     * 自定义日期选中拦截规则
     *
     * 1.过期日期不能选中
     * 2.不在最小日期到最大日期范围内的日期不能选中
     * 3.禁止选择的日期不能选中
     */
    private fun isInterceptSelect(calendarDay: CalendarDay): Boolean {
        return calendarDay.isOverDue
                || isOutOfRange(calendarDay)
                || disableCalendarDays?.contains(calendarDay) ?: false
    }


    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect, isSelected: Boolean) {
        if (isSelected) {
            //绘制选中背景
            selectedBgDrawable.bounds = rect
            selectedBgDrawable.draw(canvas)
        }
        when {
            isSelected -> {
                setPaintColor(selectTextColor)
            }
            /**
             * 展示的日期过期(比当前日期小)
             * ||超过日期展示范围
             * ||不是当前月份的日期
             * ||展示的日期是禁止选择的日期
             */
            calendarDay.isOverDue
                    || isOutOfRange(calendarDay)
                    || calendarDay.dayOwner != CalendarDay.DayOwner.THIS_MONTH
                    || disableCalendarDays?.contains(calendarDay) ?: false -> {
                setPaintColor(disClickTextColor)
            }
            else -> {
                if (calendarDay.isWeekend) {
                    setPaintColor(weekendTextColor)
                } else {
                    setPaintColor(defaultTextColor)
                }
            }
        }
        dayTextPaint.getFontMetrics(fm)
        val baseLine = (rect.centerY() + (fm.descent - fm.ascent) / 2 - fm.descent)
        val dayText: String = String.format(Locale.CANADA, "%d", calendarDay.day)
        canvas.drawText(dayText, rect.centerX().toFloat(), baseLine, dayTextPaint)

    }

    private fun initDrawable() {
        selectedBgDrawable = ContextCompat.getDrawable(context, R.drawable.drawable_calendar_selected_bg)!!
    }

    private fun initPaint() {
        dayTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dayTextPaint.textSize = Util.sp2px(context, 13f)
        dayTextPaint.textAlign = Paint.Align.CENTER
        fm = Paint.FontMetrics()
    }

    private fun setPaintColor(color: Int) {
        dayTextPaint.color = color
    }
}