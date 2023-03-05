package com.sy007.calendar.widget.help

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.*
import com.sy007.calendar.OnCalendarDayClickListener
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.widget.BaseMonthView

/**
 * @author sy007
 * @date 2023/03/03
 */
class MonthViewHolder(
    itemView: View,
    private val adapter: CalendarAdapter,
    private val headerViewBinder: MonthHeaderViewBinder<View>?,
    private val monthViewBinder: MonthViewBinder<BaseMonthView>,
    private val footerViewBinder: MonthFooterViewBinder<View>?
) : RecyclerView.ViewHolder(itemView) {

    internal val headerView = itemView.findViewById<View>(adapter.headerViewId)
    internal val footerView = itemView.findViewById<View>(adapter.footerViewId)
    internal val monthView = itemView.findViewById<BaseMonthView>(adapter.monthViewId)


    fun bindMonth(calendarDay: CalendarDay, calendarConfig: CalendarConfig) {
        headerViewBinder?.onBind(headerView, calendarDay)
        monthView.apply {
            onClickListener = object : OnCalendarDayClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClick(calendarDay: CalendarDay) {
                    adapter.notifyDataSetChanged()
                }
            }
            init(calendarDay, calendarConfig)
            monthViewBinder.onBind(this, calendarDay)
        }
        footerViewBinder?.onBind(footerView, calendarDay)
    }
}