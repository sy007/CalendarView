package com.sy007.calendar.widget.help

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.widget.BaseMonthView
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.ScrollMode
import com.sy007.calendar.widget.CalendarView

internal class CalendarLayoutManager(
    private val calendarView: CalendarView,
    @RecyclerView.Orientation orientation: Int
) :
    LinearLayoutManager(calendarView.context, orientation, false) {

    companion object {
        private const val NO_INDEX = -1
    }

    private val adapter: CalendarAdapter
        get() = calendarView.adapter as CalendarAdapter

    private val context: Context
        get() = calendarView.context


    fun smoothScrollToMonth(day: CalendarDay) {
        val monthPosition = adapter.getAdapterPosition(day)
        if (monthPosition == NO_INDEX) return
        startSmoothScroll(CalendarSmoothScroller(monthPosition, null))
    }

    fun scrollToMonth(day: CalendarDay) {
        val monthPosition = adapter.getAdapterPosition(day)
        if (monthPosition == NO_INDEX) return
        scrollToPositionWithOffset(monthPosition, 0)
        calendarView.post { adapter.notifyMonthScrollListener() }
    }

    fun smoothScrollToDay(day: CalendarDay) {
        val monthPosition = adapter.getAdapterPosition(day)
        if (monthPosition == NO_INDEX) return
        val isPaged = calendarView.scrollMode == ScrollMode.PAGE
        startSmoothScroll(CalendarSmoothScroller(monthPosition, if (isPaged) null else day))
    }

    fun scrollToDay(day: CalendarDay) {
        val monthPosition = adapter.getAdapterPosition(day)
        if (monthPosition == NO_INDEX) return
        scrollToPositionWithOffset(monthPosition, 0)
        if (calendarView.scrollMode == ScrollMode.PAGE) {
            calendarView.post { adapter.notifyMonthScrollListener() }
        } else {
            calendarView.post {
                val vh =
                    calendarView.findViewHolderForAdapterPosition(monthPosition) as? MonthViewHolder
                        ?: return@post
                val offset = calculateDayViewOffsetInParent(day, vh.monthView as BaseMonthView)
                scrollToPositionWithOffset(monthPosition, -offset)
                calendarView.post { adapter.notifyMonthScrollListener() }
            }
        }
    }


    private inner class CalendarSmoothScroller(position: Int, val day: CalendarDay?) :
        LinearSmoothScroller(context) {

        init {
            targetPosition = position
        }

        override fun getVerticalSnapPreference(): Int = SNAP_TO_START

        override fun getHorizontalSnapPreference(): Int = SNAP_TO_START

        override fun calculateDyToMakeVisible(view: View, snapPreference: Int): Int {
            val dy = super.calculateDyToMakeVisible(view, snapPreference)
            if (day == null) {
                return dy
            }
            val offset = calculateDayViewOffsetInParent(day, view as BaseMonthView)
            return dy - offset
        }

        override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
            val dx = super.calculateDxToMakeVisible(view, snapPreference)
            if (day == null) {
                return dx
            }
            val offset = calculateDayViewOffsetInParent(day, view as BaseMonthView)
            return dx - offset
        }
    }

    private fun calculateDayViewOffsetInParent(day: CalendarDay, monthView: BaseMonthView): Int {
        val dayRect = Rect()
        monthView.getDayRect(day, dayRect)
        return if (RecyclerView.VERTICAL == orientation) {
            dayRect.top
        } else {
            dayRect.left
        }
    }
}
