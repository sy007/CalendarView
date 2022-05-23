package com.sy007.calendar.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.*
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.ScrollMode
import com.sy007.calendar.widget.help.CalendarAdapter
import com.sy007.calendar.widget.help.CalendarLayoutManager
import com.sy007.calendar.widget.help.CalenderPageSnapHelper
import com.sy007.calendar.widget.help.MonthHeaderViewDecoration

/**
 * @author sy007
 * @date 2022/4/13
 */
class CalendarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {
    private val calenderPageSnapHelper: CalenderPageSnapHelper = CalenderPageSnapHelper()
    private var _onScrollListener: OnScrollListener? = null
    private val calendarLayoutManager: CalendarLayoutManager?
        get() = layoutManager as? CalendarLayoutManager
    private val mCalendarAdapter: CalendarAdapter?
        get() = adapter as? CalendarAdapter
    internal var monthHeaderViewDecoration: MonthHeaderViewDecoration? = null
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
        clearMonthHeaderViewDecoration()
        scrollMode = monthConfig.scrollMode
        headerViewBinder?.apply {
            monthHeaderViewDecoration = MonthHeaderViewDecoration(this as MonthHeaderViewBinder<View>).apply {
                addItemDecoration(this)
            }
        }
        layoutManager = CalendarLayoutManager(this, monthConfig.orientation)
        _onScrollListener = object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mCalendarAdapter?.notifyMonthScrollListener()
            }
        }.apply {
            addOnScrollListener(this)
        }
        adapter = CalendarAdapter(monthConfig,
                monthViewBinder as MonthViewBinder<BaseMonthView>)
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

    fun notifyCalendarChanged() {
        adapter?.apply {
            notifyDataSetChanged()
        }
    }

    private fun clearMonthHeaderViewDecoration() {
        monthHeaderViewDecoration?.apply {
            removeItemDecoration(this)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearMonthHeaderViewDecoration()
        _onScrollListener?.apply {
            removeOnScrollListener(this)
        }
    }
}