package com.sy007.calendar.simple.single

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.simple.R
import com.sy007.calendar.widget.SingleMonthView
import com.sy007.calendar.simple.utils.LunarCalendar
import com.sy007.calendar.utils.Util
import java.util.*
import kotlin.math.abs

/**
 * Created by sy007 on 2021/8/17.
 */
class SingleMonthViewSimple2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SingleMonthView(context, attrs, defStyleAttr) {
    private val monthTextColor = Color.parseColor("#262A86E8")
    private val daySelectTextColor = Color.parseColor("#FFFFFF")
    private val disClickTextColor = Color.parseColor("#BBBBBB")
    private val defaultTextColor = Color.parseColor("#000000")
    private val lunarSelectTextColor = Color.parseColor("#2A86E8")
    private lateinit var dayTextPaint: Paint
    private lateinit var lunarTextPaint: Paint
    private lateinit var monthTextPaint: Paint
    private lateinit var selectedBgDrawable: Drawable
    private lateinit var fm: Paint.FontMetrics
    private var dayTextWidth: Float = 0F
    private var dayTextHeight: Float = 0F

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
     */
    private fun isInterceptSelect(calendarDay: CalendarDay): Boolean {
        return isOutOfRange(calendarDay)
    }

    override fun onDrawMonthBefore(canvas: Canvas, calendarDay: CalendarDay) {
        super.onDrawMonthBefore(canvas, calendarDay)
        monthTextPaint.getFontMetrics(fm)
        val monthBaseLine = ((height - paddingTop - paddingBottom) / 2 + (fm.descent - fm.ascent) / 2 - fm.descent)
        canvas.drawText((calendarDay.month + 1).toString(), (width - paddingLeft - paddingRight) / 2F,
                monthBaseLine, monthTextPaint)
    }

    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect, isSelected: Boolean) {
        val verticalOffset = Util.dip2px(context, 5F)
        if (isSelected) {
            val dayHeight = (dayTextHeight + verticalOffset * 2)
            val dayWidth = (dayTextWidth + verticalOffset * 2)
            val radius = dayWidth.coerceAtMost(dayHeight) / 2F
            val centerX = rect.centerX()
            val centerY = rect.top + dayHeight / 2F
            //绘制选中背景
            selectedBgDrawable.setBounds(
                    (centerX - radius).toInt(),
                    (centerY - radius).toInt(),
                    (centerX + radius).toInt(),
                    (centerY + radius).toInt())
            selectedBgDrawable.draw(canvas)
        }
        when {
            isSelected -> {
                dayTextPaint.color = daySelectTextColor
                lunarTextPaint.color = lunarSelectTextColor
            }
            /**
             * 超过日期展示范围||不是当前月份的日期
             */
            isOutOfRange(calendarDay)
                    || calendarDay.dayOwner != CalendarDay.DayOwner.THIS_MONTH
            -> {
                setPaintColor(disClickTextColor)
            }
            else -> {
                setPaintColor(defaultTextColor)
            }
        }
        val dayText: String = String.format(Locale.CANADA, "%d", calendarDay.day)
        dayTextPaint.getFontMetrics(fm)
        canvas.drawText(dayText, rect.centerX().toFloat(), rect.top + verticalOffset + abs(fm.ascent), dayTextPaint)

        val lunarText = LunarCalendar.getLunarText(calendarDay.year, calendarDay.month + 1, calendarDay.day)
        lunarTextPaint.getFontMetrics(fm)
        canvas.drawText(lunarText, rect.centerX().toFloat(), rect.bottom - verticalOffset - fm.descent, lunarTextPaint)
    }

    private fun initDrawable() {
        selectedBgDrawable = ContextCompat.getDrawable(context, R.drawable.drawable_calendar_circle_selected_bg)!!
    }

    private fun initPaint() {
        dayTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = Util.sp2px(context, 22f)
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }

        fm = Paint.FontMetrics()

        //以两位数的宽度作为日期的宽度
        dayTextWidth = dayTextPaint.measureText("11")
        dayTextPaint.getFontMetrics(fm)
        dayTextHeight = fm.descent - fm.ascent

        lunarTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = Util.sp2px(context, 13f)
            textAlign = Paint.Align.CENTER
        }

        monthTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = Util.sp2px(context, 260F)
            textAlign = Paint.Align.CENTER
            color = monthTextColor
        }

    }

    private fun setPaintColor(color: Int) {
        dayTextPaint.color = color
        lunarTextPaint.color = color
    }
}