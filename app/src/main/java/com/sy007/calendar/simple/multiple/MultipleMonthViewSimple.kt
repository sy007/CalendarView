package com.sy007.calendar.simple.multiple
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.widget.MultipleMonthView
import com.sy007.calendar.isWeekend
import com.sy007.calendar.simple.R
import com.sy007.calendar.utils.Util
import java.util.*

/**
 * Created by sy007 on 5/14/22.
 */
class MultipleMonthViewSimple @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : MultipleMonthView(context, attrs, defStyleAttr) {
    private val selectTextColor = Color.parseColor("#FFFFFF")
    private val defaultTextColor = Color.parseColor("#000000")
    private val weekendTextColor = Color.parseColor("#FF6E00")
    private lateinit var dayTextPaint: Paint
    private lateinit var selectedBgDrawable: Drawable
    private lateinit var fm: Paint.FontMetrics

    init {
        initPaint()
        initDrawable()
    }


    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect, isSelected: Boolean) {
        if (isSelected) {
            val size = rect.width().coerceAtMost(rect.height())
            val radius = size / 2
            val centerX = rect.centerX()
            val centerY = rect.centerY()
            //绘制选中背景
            selectedBgDrawable.setBounds(
                    centerX - radius,
                    centerY - radius,
                    centerX + radius,
                    centerY + radius)
            selectedBgDrawable.draw(canvas)
        }
        when {
            isSelected -> {
                setPaintColor(selectTextColor)
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
        val baseLine = (rect.centerY() - fm.ascent / 2 - fm.descent / 2)
        val dayText: String = String.format(Locale.CANADA, "%d", calendarDay.day)
        canvas.drawText(dayText, rect.centerX().toFloat(), baseLine, dayTextPaint)

    }

    private fun initDrawable() {
        selectedBgDrawable = ContextCompat.getDrawable(context, R.drawable.drawable_calendar_circle_selected_bg)!!
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