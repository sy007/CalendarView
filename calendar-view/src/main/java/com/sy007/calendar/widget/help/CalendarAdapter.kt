package com.sy007.calendar.widget.help

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.util.forEach
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.*
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.utils.Util
import com.sy007.calendar.widget.BaseMonthView
import com.sy007.calendar.widget.CalendarView
import java.util.*

/**
 * @author sy007
 * @date 2019/4/20
 */
class CalendarAdapter(
    private val calendarConfig: CalendarConfig,
    private val headerViewBinder: MonthHeaderViewBinder<View>?,
    private val monthViewBinder: MonthViewBinder<BaseMonthView>,
    private val footerViewBinder: MonthFooterViewBinder<View>?
) :
    RecyclerView.Adapter<MonthViewHolder>() {
    companion object {
        const val MONTH_IN_YEAR = 12
    }

    private val startCalendar: Calendar = calendarConfig.startCalendar
    private val endCalendar: Calendar = calendarConfig.endCalendar
    private val monthCount = Util.diffMonth(startCalendar, endCalendar) + 1
    private var itemCount: Int = monthCount
    private var visibleCalendarDay: CalendarDay? = null
    private lateinit var calendarView: CalendarView
    private val calendarLayoutManager: CalendarLayoutManager
        get() = calendarView.layoutManager as CalendarLayoutManager
    private val positionToCalendarDay: SparseArray<CalendarDay> = SparseArray()

    var headerViewId = ViewCompat.generateViewId()
    var footerViewId = ViewCompat.generateViewId()
    var monthViewId = ViewCompat.generateViewId()
    var attached = false

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attached = true
        val count = getItemCount()
        for (position in 0 until count) {
            positionToCalendarDay.put(position, computeCalendarDayByPosition(position))
        }
        @Suppress("CAST_NEVER_SUCCEEDS")
        calendarView = recyclerView as CalendarView
        calendarView.apply {
            post {
                notifyMonthScrollListener()
            }
        }
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        positionToCalendarDay.clear()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val rootView = LinearLayout(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }
        headerViewBinder?.apply {
            val headerView = create(rootView)
            if (headerView.id == View.NO_ID) {
                headerView.id = headerViewId
            } else {
                headerViewId = headerView.id
            }
            rootView.addView(headerView, headerView.ifNullCreateLp())
        }

        monthViewBinder.apply {
            val monthView = monthViewBinder.create(rootView)
            if (monthView.id == View.NO_ID) {
                monthView.id = monthViewId
            } else {
                monthViewId = monthView.id
            }
            rootView.addView(monthView, monthView.ifNullCreateLp())
        }

        footerViewBinder?.apply {
            val footerView = create(rootView)
            if (footerView.id == View.NO_ID) {
                footerView.id = footerViewId
            } else {
                footerViewId = footerView.id
            }
            rootView.addView(footerView, footerView.ifNullCreateLp())
        }
        return MonthViewHolder(rootView, this, headerViewBinder, monthViewBinder, footerViewBinder)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val calendarDay = computeCalendarDayByPosition(position)
        holder.bindMonth(calendarDay, calendarConfig)
    }

    private fun computeCalendarDayByPosition(position: Int): CalendarDay {
        val year =
            (startCalendar.get(Calendar.MONTH) + position) / MONTH_IN_YEAR + startCalendar.get(
                Calendar.YEAR
            )
        val month = (startCalendar.get(Calendar.MONTH) + (position % MONTH_IN_YEAR)) % MONTH_IN_YEAR
        return if (startCalendar.get(Calendar.YEAR) == year && startCalendar.get(Calendar.MONTH) == month) {
            CalendarDay(year, month, startCalendar.get(Calendar.DAY_OF_MONTH))
        } else if (endCalendar.get(Calendar.YEAR) == year && endCalendar.get(Calendar.MONTH) == month) {
            CalendarDay(year, month, endCalendar.get(Calendar.DAY_OF_MONTH))
        } else {
            CalendarDay(year, month, 1)
        }
    }


    override fun getItemCount(): Int {
        return itemCount
    }


    fun notifyMonthScrollListener() {
        if (!attached) return
        if (calendarView.isAnimating) {
            calendarView.itemAnimator?.isRunning {
                notifyMonthScrollListener()
            }
            return
        }
        val visibleItemPos = calendarLayoutManager.findFirstVisibleItemPosition()
        if (visibleItemPos != RecyclerView.NO_POSITION) {
            val calendarDay = computeCalendarDayByPosition(visibleItemPos)
            if (calendarDay != visibleCalendarDay) {
                calendarView.monthScrollListener?.onScroll(calendarDay)
                visibleCalendarDay = calendarDay
                if (calendarView.scrollMode == ScrollMode.PAGE && calendarView.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    val visibleVH = calendarView.findViewHolderForAdapterPosition(visibleItemPos) as? MonthViewHolder ?: return
                    visibleVH.itemView.requestLayout()
                }
            }
        }
    }


    fun getAdapterPosition(calendarDay: CalendarDay): Int {
        var result = -1
        positionToCalendarDay.forEach { key, value ->
            if (calendarDay.sameMonth(value)) {
                result = key
                return@forEach
            }
        }
        return result
    }
}


