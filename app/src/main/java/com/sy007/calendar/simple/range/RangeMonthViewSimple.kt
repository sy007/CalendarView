package com.sy007.calendar.simple.range

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.sy007.calendar.*
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.widget.RangeMonthView
import com.sy007.calendar.R
import com.sy007.calendar.simple.utils.LunarCalendar
import com.sy007.calendar.utils.Util
import java.util.*
import kotlin.math.abs

/**
 * 范围选择
 * Created by sy007 on 2021/8/17.
 */
class RangeMonthViewSimple @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RangeMonthView(context, attrs, defStyleAttr) {
    private val selectTextColor = Color.parseColor("#FFFFFF")
    private val disClickTextColor = Color.parseColor("#BBBBBB")
    private val defaultTextColor = Color.parseColor("#000000")
    private val weekendTextColor = Color.parseColor("#FF6E00")
    private lateinit var topTextPaint: Paint
    private lateinit var dayTextPaint: Paint
    private lateinit var bottomTextPaint: Paint

    private lateinit var selectedFirstBgDrawable: Drawable
    private lateinit var selectedLastBgDrawable: Drawable

    private lateinit var selectedRangeDrawable: Drawable
    private lateinit var fm: Paint.FontMetrics


    override fun onInterceptSelect(calendarDay: CalendarDay): Boolean {
        return super.onInterceptSelect(calendarDay) || calendarDay.isOverDue
    }

    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect, isSelected: Boolean) {

        if (isFirstSelected(calendarDay)) {
            selectedFirstBgDrawable.bounds = rect
            selectedFirstBgDrawable.draw(canvas)
        }
        if (isLastSelected(calendarDay)) {
            selectedLastBgDrawable.bounds = rect
            selectedLastBgDrawable.draw(canvas)
        }

        val dayText: String = if (calendarDay.isToday) {
            "今天"
        } else {
            String.format(Locale.CANADA, "%d", calendarDay.day)
        }
        if (isSelected) {
            setPaintColor(selectTextColor)
        } else {
            if (calendarDay.isOverDue || isOutOfRange(calendarDay)) {
                setPaintColor(disClickTextColor)
            } else {
                if (calendarDay.isWeekend) {
                    setPaintColor(weekendTextColor)
                } else {
                    setPaintColor(defaultTextColor)
                }
            }
        }
        //从上往下绘制
        val topTextMarginTop = Util.dip2px(context, 8f)
        val dayTextMarginTop = Util.dip2px(context, 5f)
        val bottomTextMarginTop = Util.dip2px(context, 5f)
        topTextPaint.getFontMetrics(fm)
        //获取公历节日
        val gregorianFestival = LunarCalendar.gregorianFestival(calendarDay.month + 1, calendarDay.day)
        val dayTextTop: Float = if (!TextUtils.isEmpty(gregorianFestival)) {
            val topTextBaseLine = topTextMarginTop + rect.top + abs(fm.ascent)
            canvas.drawText(gregorianFestival, rect.centerX().toFloat(), topTextBaseLine, topTextPaint)
            topTextBaseLine + fm.descent + dayTextMarginTop
        } else {
            val topTextHeight = fm.descent - fm.ascent
            rect.top + topTextMarginTop + topTextHeight + dayTextMarginTop
        }
        dayTextPaint.getFontMetrics(fm)
        canvas.drawText(dayText, rect.centerX().toFloat(), dayTextTop + abs(fm.ascent), dayTextPaint)

        val dayTextHeight = fm.descent - fm.ascent
        val bottomTextTop = dayTextTop + dayTextHeight + bottomTextMarginTop
        bottomTextPaint.getFontMetrics(fm)
        if (isFirstSelected(calendarDay)) {
            canvas.drawText(BOTTOM_IN_ROOM_TEXT, rect.centerX().toFloat(), bottomTextTop + abs(fm.ascent), bottomTextPaint)
        }
        if (isLastSelected(calendarDay)) {
            canvas.drawText(BOTTOM_OUT_ROOM_TEXT, rect.centerX().toFloat(), bottomTextTop + abs(fm.ascent), bottomTextPaint)
        }
    }

    override fun onDrawSelectedRange(canvas: Canvas, rect: Rect) {
        selectedRangeDrawable.bounds = rect
        selectedRangeDrawable.draw(canvas)
    }

    private fun initDrawable() {
        selectedFirstBgDrawable = ContextCompat.getDrawable(context, R.drawable.drawable_calendar_range_first_selected_bg)!!
        selectedLastBgDrawable = ContextCompat.getDrawable(context, R.drawable.drawable_calendar_range_last_selected_bg)!!
        selectedRangeDrawable = ContextCompat.getDrawable(context, R.drawable.drawable_calendar_selected_range_bg)!!
    }

    private fun initPaint() {
        topTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        topTextPaint.textSize = Util.sp2px(context, 10f)
        topTextPaint.textAlign = Paint.Align.CENTER
        dayTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dayTextPaint.textSize = Util.sp2px(context, 13f)
        dayTextPaint.textAlign = Paint.Align.CENTER
        bottomTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bottomTextPaint.textSize = Util.sp2px(context, 10f)
        bottomTextPaint.textAlign = Paint.Align.CENTER
        fm = Paint.FontMetrics()
    }

    private fun setPaintColor(color: Int) {
        topTextPaint.color = color
        dayTextPaint.color = color
        bottomTextPaint.color = color
    }

    companion object {
        private const val BOTTOM_IN_ROOM_TEXT = "入住"
        private const val BOTTOM_OUT_ROOM_TEXT = "离店"
    }

    init {
        initPaint()
        initDrawable()
    }
}