package com.sy007.calendar.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.*
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.widget.help.*
import com.sy007.calendar.widget.help.CalendarLayoutManager
import com.sy007.calendar.widget.help.CalenderPageSnapHelper
import com.sy007.calendar.widget.help.StickHeaderDecoration

/**
 * @author sy007
 * @date 2022/4/13
 */
class CalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var stickHeaderClickHandle = false
    private val calenderPageSnapHelper: CalenderPageSnapHelper = CalenderPageSnapHelper()
    private var _onScrollListener: OnScrollListener? = null
    private val calendarLayoutManager: CalendarLayoutManager?
        get() = layoutManager as? CalendarLayoutManager
    private val calendarAdapter: CalendarAdapter?
        get() = adapter as? CalendarAdapter
    private var stickHeaderDecoration: StickHeaderDecoration? = null
    internal var scrollMode: ScrollMode = ScrollMode.CONTINUITIES
        set(value) {
            field = value
            if (ScrollMode.PAGE == value) {
                calenderPageSnapHelper.attachToRecyclerView(this)
            }
        }

    /**
     * 设置月份视图头布局
     */
    var headerViewBinder: MonthHeaderViewBinder<out View>? = null

    /**
     * 设置月份视图脚布局
     */
    var footerViewBinder: MonthFooterViewBinder<out View>? = null

    /**
     * 设置月份视图
     */
    var monthViewBinder: MonthViewBinder<out BaseMonthView>? = null

    /**
     * 设置月份视图滚动变化监听
     */
    var monthScrollListener: OnMonthScrollListener? = null

    @Suppress("UNCHECKED_CAST")
    fun setUp(monthConfig: CalendarConfig) {
        if (monthViewBinder == null) {
            return
        }
        clearMonthHeaderViewStick()
        scrollMode = monthConfig.scrollMode
        if ((headerViewBinder?.isStick() == true)) {
            stickHeaderDecoration = StickHeaderDecoration().apply {
                addItemDecoration(this)
            }
        }
        layoutManager = CalendarLayoutManager(this, monthConfig.orientation)
        _onScrollListener = object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                calendarAdapter?.notifyMonthScrollListener()
            }
        }.apply {
            addOnScrollListener(this)
        }
        adapter = CalendarAdapter(
            monthConfig,
            headerViewBinder as? MonthHeaderViewBinder<View>,
            monthViewBinder as MonthViewBinder<BaseMonthView>,
            footerViewBinder as? MonthFooterViewBinder<View>
        )
    }

    /**
     * 平滑滚动到指定月份
     */
    fun smoothScrollToMonth(calendarDay: CalendarDay) {
        calendarLayoutManager?.smoothScrollToMonth(calendarDay)
    }

    /**
     * 滚动到指定月份
     */
    fun scrollToMonth(calendarDay: CalendarDay) {
        calendarLayoutManager?.scrollToMonth(calendarDay)
    }

    /**
     * 平滑滚动到指定日期
     */
    fun smoothScrollToDay(calendarDay: CalendarDay) {
        calendarLayoutManager?.smoothScrollToDay(calendarDay)
    }

    /**
     * 滚动到指定日期
     */
    fun scrollToDay(calendarDay: CalendarDay) {
        calendarLayoutManager?.scrollToDay(calendarDay)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyCalendarChanged() {
        calendarAdapter?.apply {
            notifyDataSetChanged()
        }
    }

    private fun clearMonthHeaderViewStick() {
        stickHeaderDecoration?.apply {
            removeItemDecoration(this)
        }
    }


    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val monthHeaderViewStick = stickHeaderDecoration ?: return super.onInterceptTouchEvent(e)
        val stickHeaderRect: Rect = monthHeaderViewStick.stickHeaderRect
        if (stickHeaderRect.isEmpty || NO_POSITION == monthHeaderViewStick.stickHeaderPos) {
            return super.onInterceptTouchEvent(e)
        }
        when (e.action) {
            MotionEvent.ACTION_DOWN -> if (stickHeaderRect.contains(e.x.toInt(), e.y.toInt())) {
                return true
            }
        }
        return super.onInterceptTouchEvent(e)
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val monthHeaderViewStick = stickHeaderDecoration ?: return super.onTouchEvent(ev)
        val stickHeaderRect: Rect = monthHeaderViewStick.stickHeaderRect
        if (stickHeaderRect.isEmpty || NO_POSITION == monthHeaderViewStick.stickHeaderPos) {
            return super.onTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                stickHeaderClickHandle = false
                if (adapter?.itemCount != 0 && stickHeaderRect.contains(
                        ev.x.toInt(),
                        ev.y.toInt()
                    )
                ) {
                    stickHeaderClickHandle = true
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> if (stickHeaderClickHandle) {
                return if (!stickHeaderRect.contains(ev.x.toInt(), ev.y.toInt())) {
                    val cancel = MotionEvent.obtain(ev)
                    cancel.action = MotionEvent.ACTION_CANCEL
                    super.dispatchTouchEvent(cancel)
                    val down = MotionEvent.obtain(ev)
                    down.action = MotionEvent.ACTION_DOWN
                    super.dispatchTouchEvent(down)
                } else {
                    true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (calendarAdapter?.itemCount != 0 && stickHeaderRect.contains(
                        ev.x.toInt(),
                        ev.y.toInt()
                    )
                ) {
                    val holder =
                        findViewHolderForAdapterPosition(monthHeaderViewStick.stickHeaderPos) as MonthViewHolder
                    if (holder.headerView.hasOnClickListeners()) {
                        holder.headerView.performClick()
                    }
                    stickHeaderClickHandle = false
                    return true
                }
                stickHeaderClickHandle = false
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearMonthHeaderViewStick()
        _onScrollListener?.apply {
            removeOnScrollListener(this)
        }
    }
}