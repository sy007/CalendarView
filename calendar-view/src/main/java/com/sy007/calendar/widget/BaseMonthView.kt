package com.sy007.calendar.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sy007.calendar.*
import com.sy007.calendar.OnCalendarDayClickListener
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.CalendarConfig
import com.sy007.calendar.utils.Util
import java.util.*
import kotlin.math.abs

/**
 * 月份视图基类
 * @author sy007
 * @date 2022/4/7
 */
abstract class BaseMonthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val FIXED_ROW_NUM = 6
    }

    private val dividerPaint: Paint

    private lateinit var calendarConfig: CalendarConfig

    /**
     * 当前展示的月份
     */
    private lateinit var curCalendarDay: CalendarDay

    /**
     * 展示的最小月份
     */
    private lateinit var startCalendarDay: CalendarDay

    /**
     * 展示的最大月份
     */
    private lateinit var endCalendarDay: CalendarDay

    /**
     * 当前展示的月份的前一个月
     */
    private var preCalendarDay: CalendarDay? = null

    /**
     * 当前展示的月份的后一个月
     */
    private var nextCalendarDay: CalendarDay? = null

    /**
     * 日期绘制范围
     */
    private val dayRang: Rect

    internal var onClickListener: OnCalendarDayClickListener? = null

    /**
     * 月份展示几列
     */
    protected var columnNum = 7
        private set

    /**
     * 日期宽度
     */
    protected var dayWidth = 0
        private set

    /**
     * 月份展示几行
     */
    protected var rowNum = FIXED_ROW_NUM
        private set

    /**
     * 行高
     */
    var rowHeight: Int

    /**
     * 月份中每一行的间隔
     */
    var dividerHeight: Int

    /**
     * 月份中每一行的间隔颜色
     */
    var dividerColor: Int

    private val onDrawDayBlock = { canvas: Canvas, calendarDay: CalendarDay, rect: Rect ->
        onDrawDay(canvas, calendarDay, rect)
        if (calendarDay.isLastColumn()) {
            drawDivider(canvas)
        }
    }

    init {
        @SuppressLint("CustomViewStyleable")
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MonthView)
        rowHeight = ta.getFloat(R.styleable.MonthView_rowHeight, Util.dip2px(context, 64f)).toInt()
        dividerHeight = ta.getDimension(R.styleable.MonthView_dividerHeight, 0f).toInt()
        dividerColor = ta.getColor(R.styleable.MonthView_dividerColor, 0)
        ta.recycle()
        dayRang = Rect()
        dividerPaint = Paint()
        dividerPaint.color = dividerColor
    }

    fun init(curCalendarDay: CalendarDay, calendarConfig: CalendarConfig) {
        this.curCalendarDay = curCalendarDay
        this.calendarConfig = calendarConfig
        calendarConfig.apply {
            startCalendarDay = CalendarDay(startCalendar)
            endCalendarDay = CalendarDay(endCalendar)
            rowNum = if (getAndFixHeightMode() == HeightMode.DYNAMIC) {
                when {
                    curCalendarDay.sameMonth(startCalendarDay) -> {
                        calculateNumRows(curCalendarDay.day, curCalendarDay.lastDayOfMonth, curCalendarDay)
                    }
                    curCalendarDay.sameMonth(endCalendarDay) -> {
                        calculateNumRows(curCalendarDay.firstDayOfMonth, curCalendarDay.day, curCalendarDay)
                    }
                    else -> {
                        calculateNumRows(curCalendarDay.firstDayOfMonth, curCalendarDay.lastDayOfMonth, curCalendarDay)
                    }
                }
            } else {
                FIXED_ROW_NUM
            }
            if (isDisplayExtraDay) {
                preCalendarDay = curCalendarDay.createPreCalendarDay()
                nextCalendarDay = curCalendarDay.createNextCalendarDay()
            }
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, getRealHeight())
    }

    internal fun getRealHeight() = ((rowNum - 1) * dividerHeight + rowNum * rowHeight +
            paddingTop + paddingBottom)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //这里宽度存在除不尽情况
        dayWidth = (w - (paddingLeft + paddingRight)) / columnNum
        //将多余的宽度平均分给两边的padding
        val halfRemainWidth = (w - paddingLeft - paddingRight) % columnNum / 2
        setPadding(paddingLeft + halfRemainWidth, paddingTop, paddingRight + halfRemainWidth, paddingBottom)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_UP) {
            val calendarDay = getClickCalendarDay(event.x, event.y)
            if (calendarDay != null && !onInterceptSelect(calendarDay)) {
                onClickCalendarDay(calendarDay)
                onClickListener?.onClick(calendarDay)
            }
        }
        return true
    }

    /**
     * 日期被点击回调
     *
     * @param calendarDay 点击的日期
     */
    abstract fun onClickCalendarDay(calendarDay: CalendarDay)

    /**
     * 是否拦截日期选中事件，如果拦截则不会回调日期选中事件
     * 你可以在此方法中拦截不想让用户点击的日期
     * @return true 表示拦截，false 表示不拦截
     */
    open fun onInterceptSelect(calendarDay: CalendarDay): Boolean {
        return false
    }

    private fun getClickCalendarDay(x: Float, y: Float): CalendarDay? {
        var pointY = y
        if (x < paddingLeft || x > width - paddingRight) {
            return null
        }
        if (pointY < paddingTop || pointY > height - paddingBottom) {
            return null
        }
        pointY -= paddingTop.toFloat()

        val clickCalendarDay = CalendarDay(curCalendarDay.year, curCalendarDay.month, 1)
        //计算当前月份[firstDayOfMonth-lastDayOfMonth]占用多少行
        val totalRowNum = calculateNumRows(clickCalendarDay.firstDayOfMonth, curCalendarDay.lastDayOfMonth, curCalendarDay)
        /*
          考虑到非满月展示，如果当前点击的是最小月份
          这里需要给pointY+(totalRowNum - rowNum) * (rowHeight + dividerHeight)
         */
        pointY = if (HeightMode.DYNAMIC == calendarConfig.getAndFixHeightMode() && curCalendarDay.sameMonth(startCalendarDay)) {
            pointY + (totalRowNum - rowNum) * (rowHeight + dividerHeight)
        } else {
            pointY
        }

        val row = (pointY / (rowHeight + dividerHeight)).toInt()
        val column = ((x - paddingLeft) * columnNum / (width - paddingLeft - paddingRight)).toInt() + 1
        var day = column - clickCalendarDay.getDayOffset(clickCalendarDay.firstDayOfMonth)
        day += row * columnNum
        return when {
            day > clickCalendarDay.lastDayOfMonth -> {
                nextCalendarDay?.run {
                    clickCalendarDay.year = year
                    clickCalendarDay.month = month
                    clickCalendarDay.day = day - curCalendarDay.lastDayOfMonth
                    clickCalendarDay
                }
            }
            day < 1 -> {
                preCalendarDay?.run {
                    clickCalendarDay.year = year
                    clickCalendarDay.month = month
                    clickCalendarDay.day = lastDayOfMonth - abs(day)
                    clickCalendarDay
                }
            }
            else -> {
                clickCalendarDay.day = day
                if (!calendarConfig.isDisplayExtraDay && isOutOfRange(clickCalendarDay)) {
                    return null
                }
                clickCalendarDay
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!this::calendarConfig.isInitialized) {
            return
        }
        onDrawMonthBefore(canvas, curCalendarDay)
        drawMonth(canvas)
        onDrawMonthAfter(canvas, curCalendarDay)
    }

    /**
     * 绘制日期
     *
     * @param canvas      canvas
     * @param calendarDay 待绘制的日期
     * @param rect        日期绘制范围
     */
    protected abstract fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect)

    /**
     * 绘制日期
     *
     * <p>父类定义，子类调用
     * @see SingleMonthView.onDrawDay
     * @see RangeMonthView.onDrawDay
     * @see MultipleMonthView.onDrawDay
     *
     * @param canvas      canvas
     * @param calendarDay 待绘制的日期
     * @param rect        日期绘制范围
     * @param isSelected  该日期是否选中
     */
    protected abstract fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect, isSelected: Boolean)

    /**
     * 月份日期绘制前调用
     *
     * @param calendarDay 当前展示的月份
     */
    open fun onDrawMonthBefore(canvas: Canvas, calendarDay: CalendarDay) {

    }

    /**
     * 月份日期绘制后调用
     *
     * @param calendarDay 当前展示的月份
     */
    open fun onDrawMonthAfter(canvas: Canvas, calendarDay: CalendarDay) {

    }

    /**
     * 绘制月份日期
     */
    private fun drawMonth(canvas: Canvas) {
        if (calendarConfig.isDisplayExtraDay) {
            drawStartExtraDay(canvas)
        }
        val start: Int
        val end: Int
        when {
            (curCalendarDay.sameMonth(startCalendarDay) && curCalendarDay.sameMonth(endCalendarDay)) -> {
                start = startCalendarDay.day
                end = endCalendarDay.day
                draw(canvas, curCalendarDay, start, end, onDrawDayBlock)
            }
            curCalendarDay.sameMonth(startCalendarDay) -> {
                //绘制最小月份
                start = startCalendarDay.day
                end = startCalendarDay.lastDayOfMonth
                draw(canvas, curCalendarDay, start, end, onDrawDayBlock)
            }
            curCalendarDay.sameMonth(endCalendarDay) -> {
                //绘制最大月份
                start = endCalendarDay.firstDayOfMonth
                end = endCalendarDay.day
                draw(canvas, curCalendarDay, start, end, onDrawDayBlock)
            }
            else -> {
                //绘制中间月份
                start = curCalendarDay.firstDayOfMonth
                end = curCalendarDay.lastDayOfMonth
                draw(canvas, curCalendarDay, start, end, onDrawDayBlock)
            }
        }
        if (calendarConfig.isDisplayExtraDay) {
            drawEndExtraDay(canvas)
        }
    }

    /**
     * 绘制月份开始位置的额外日期
     *
     * <p>如果当前展示的是最小月份，优先绘制最小月份当前行剩余的日期，再绘制上个月日期
     */
    private fun drawStartExtraDay(canvas: Canvas) {
        val dayOffset: Int
        if (curCalendarDay.sameMonth(startCalendarDay)) {
            val fillCount = startCalendarDay.getDayOffset(startCalendarDay.day)
            var start = startCalendarDay.day - fillCount
            val end = startCalendarDay.day - 1
            if (start < startCalendarDay.firstDayOfMonth) {
                start = startCalendarDay.firstDayOfMonth
            }
            draw(canvas, curCalendarDay, start, end, onDrawDayBlock)
            dayOffset = curCalendarDay.getDayOffset(start)
        } else {
            dayOffset = curCalendarDay.getDayOffset(curCalendarDay.firstDayOfMonth)
        }
        preCalendarDay?.let { preCalendarDay ->
            val end: Int = preCalendarDay.lastDayOfMonth
            val start = end - dayOffset + 1
            draw(canvas, preCalendarDay, start, end, onDrawDayBlock)
        }
    }

    /**
     * 绘制月份结束位置的额外日期
     *
     * <p>如果当前展示的是最大月份，优先绘制最大月份当前行剩余的日期，再绘制下个月日期
     */
    private fun drawEndExtraDay(canvas: Canvas) {
        val dayOffset: Int
        if (curCalendarDay.sameMonth(endCalendarDay)) {
            val fillCount = columnNum - endCalendarDay.getDayOffset(endCalendarDay.day) - 1
            var start = endCalendarDay.day + 1
            if (start > endCalendarDay.lastDayOfMonth) {
                start = endCalendarDay.lastDayOfMonth
            }
            var end = endCalendarDay.day + fillCount
            if (end > endCalendarDay.lastDayOfMonth) {
                end = endCalendarDay.lastDayOfMonth
            }
            draw(canvas, curCalendarDay, start, end, onDrawDayBlock)
            dayOffset = curCalendarDay.getDayOffset(end)
        } else {
            dayOffset = curCalendarDay.getDayOffset(curCalendarDay.lastDayOfMonth)
        }
        nextCalendarDay?.let { nextCalendarDay ->
            var remainderRowFillCount = columnNum - dayOffset - 1
            if (HeightMode.FIXED == calendarConfig.getAndFixHeightMode()) {
                val remainderRow = FIXED_ROW_NUM - calculateNumRows(curCalendarDay.firstDayOfMonth, curCalendarDay.lastDayOfMonth, curCalendarDay)
                remainderRowFillCount += remainderRow * columnNum
            }
            draw(canvas, nextCalendarDay, nextCalendarDay.firstDayOfMonth, remainderRowFillCount, onDrawDayBlock)
        }
    }


    protected fun draw(
            canvas: Canvas,
            calendarDay: CalendarDay,
            start: Int,
            end: Int,
            block: (canvas: Canvas, calendarDay: CalendarDay, rect: Rect) -> Unit,
            rect: Rect = this.dayRang) {
        for (day in start..end) {
            calendarDay.day = day
            getDayRect(calendarDay, rect)
            block(canvas, calendarDay, rect)
        }
    }

    /**
     * 判断指定日期是否超过[startCalendarDay-endCalendarDay]范围
     *
     * @return true 超范围，false 没有超范围
     */
    fun isOutOfRange(calendarDay: CalendarDay): Boolean {
        return calendarDay.before(startCalendarDay) || calendarDay.after(endCalendarDay)
    }

    private fun drawDivider(canvas: Canvas) {
        if (0 != dividerHeight) {
            canvas.drawRect(paddingLeft.toFloat(), dayRang.bottom.toFloat(), (width - paddingRight).toFloat(), (dayRang.bottom + dividerHeight).toFloat(), dividerPaint)
        }
    }

    /**
     * 获取CalendarDay在View上的位置
     */
    fun getDayRect(calendarDay: CalendarDay, outRect: Rect) {
        val left = paddingLeft + calendarDay.getDayOffset(calendarDay.day) * dayWidth
        val right = left + dayWidth
        //[1-rowNum]
        val row = when (calendarDay.dayOwner) {
            CalendarDay.DayOwner.PRE_MONTH -> {
                //上个月的日期永远在第一行展示
                1
            }
            CalendarDay.DayOwner.NEXT_MONTH -> {
                calculateNumRows(curCalendarDay.firstDayOfMonth, curCalendarDay.lastDayOfMonth + calendarDay.day, curCalendarDay)
            }
            else -> {
                if (calendarDay.sameMonth(startCalendarDay)) {
                    calculateNumRows(startCalendarDay.day, calendarDay.day, calendarDay)
                } else {
                    calculateNumRows(calendarDay.firstDayOfMonth, calendarDay.day, calendarDay)
                }
            }
        }
        val top = (row - 1) * (rowHeight + dividerHeight) + paddingTop
        val bottom = top + rowHeight
        outRect.set(left, top, right, bottom)
    }

    private fun CalendarConfig.getAndFixHeightMode(): HeightMode {
        if (startCalendarDay.day != startCalendarDay.firstDayOfMonth || endCalendarDay.day != endCalendarDay.lastDayOfMonth) {
            return HeightMode.DYNAMIC
        }
        return heightMode
    }

    private fun calculateNumRows(minDay: Int, maxDay: Int, calendarDay: CalendarDay): Int {
        val offset = calendarDay.getDayOffset(minDay)
        val dividend = (offset + (maxDay - minDay + 1)) / columnNum
        val remainder = (offset + (maxDay - minDay + 1)) % columnNum
        return dividend + if (remainder > 0) 1 else 0
    }

    private fun CalendarDay.isLastColumn() = columnNum - getDayOffset(day) == 1

    /**
     * 获取当前日子距离左边偏移天数，用于日期x轴坐标计算
     *
     * @param day 当前绘制的日子
     * @return    返回[0-6]
     */
    private fun CalendarDay.getDayOffset(day: Int): Int {
        val calendar = calendar
        calendar[Calendar.DAY_OF_MONTH] = day
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        return if (dayOfWeek < calendarConfig.weekStart.value) {
            dayOfWeek + 7 - calendarConfig.weekStart.value
        } else {
            dayOfWeek - calendarConfig.weekStart.value
        }
    }
}

