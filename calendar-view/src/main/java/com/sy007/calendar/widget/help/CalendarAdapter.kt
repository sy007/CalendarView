package com.sy007.calendar.widget.help

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sy007.calendar.*
import com.sy007.calendar.OnCalendarDayClickListener
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.ScrollMode
import com.sy007.calendar.utils.Util
import com.sy007.calendar.widget.BaseMonthView
import com.sy007.calendar.widget.CalendarView
import java.util.*

/**
 * @author sy007
 * @date 2019/4/20
 */
internal class CalendarAdapter(private val calendarConfig: CalendarConfig,
                               private val monthViewBinder: MonthViewBinder<BaseMonthView>) :
        RecyclerView.Adapter<CalendarAdapter.MonthViewHolder>(), MonthHeaderViewDecoration.CalendarDayCallback {
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
    private var isCalendarViewWrapHeight: Boolean? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
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
        val itemView = monthViewBinder.create(parent)
        if (itemView.layoutParams == null) {
            itemView.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT)
        }
        return MonthViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val calendarDay = computeCalendarDayByPosition(position)
        holder.monthView.apply {
            onClickListener = object : OnCalendarDayClickListener {
                override fun onClick(calendarDay: CalendarDay) {
                    notifyDataSetChanged()
                }
            }
            init(calendarDay, calendarConfig)
        }
        monthViewBinder.onBind(holder.monthView, calendarDay)
    }

    private fun computeCalendarDayByPosition(position: Int): CalendarDay {
        val year = (startCalendar.get(Calendar.MONTH) + position) / MONTH_IN_YEAR + startCalendar.get(Calendar.YEAR)
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
                if (calendarView.scrollMode == ScrollMode.PAGE) {
                    val isCalendarViewWrapHeight = isCalendarViewWrapHeight
                            ?: (calendarView.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT).also {
                                isCalendarViewWrapHeight = it
                            }
                    if (!isCalendarViewWrapHeight) return
                    val visibleVH = calendarView.findViewHolderForAdapterPosition(visibleItemPos) as? MonthViewHolder
                            ?: return
                    val headerView = calendarView.monthHeaderViewDecoration?.getHeaderViewForPosition(visibleItemPos)
                    val headerViewVerticalSpace = headerView?.run {
                        height + getVerticalMargins()
                    } ?: 0
                    val itemViewVerticalSpace = (visibleVH.monthView).run {
                        getRealHeight() + getVerticalMargins()
                    }
                    val newHeight = headerViewVerticalSpace + itemViewVerticalSpace
                    if (calendarView.height != newHeight) {
                        calendarView.updateLayoutParams { height = newHeight }
                    }
                }
            }
        }
    }


    class MonthViewHolder(itemView: View) : ViewHolder(itemView) {
        val monthView: BaseMonthView = itemView as BaseMonthView
    }

    override fun getCalendarDayByPosition(position: Int): CalendarDay = computeCalendarDayByPosition(position)

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