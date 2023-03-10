package com.sy007.calendar.simple.multiple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.*
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.ScrollMode
import com.sy007.calendar.simple.BaseActivity
import com.sy007.calendar.utils.Util
import java.util.*
import com.sy007.calendar.simple.databinding.ActivityMultipleSelectBinding
import com.sy007.calendar.simple.R

class MultipleSelectActivity : BaseActivity() {
    companion object {
        private const val TAG = "MultipleSelectActivity"
        fun start(context: Context) {
            val intent = Intent(context, MultipleSelectActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var selected: MutableSet<CalendarDay> = mutableSetOf()
    private lateinit var binding: ActivityMultipleSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultipleSelectBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setTitle("多选")
        binding.cvMultipleView.apply {
            val startCalendar = Calendar.getInstance()
            startCalendar.set(Calendar.DAY_OF_MONTH, 1)
            val endCalendar = Calendar.getInstance()
            endCalendar.add(Calendar.MONTH, 10)
            endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val calendarConfig = CalendarConfig(startCalendar, endCalendar).apply {
                orientation = RecyclerView.VERTICAL
                scrollMode = ScrollMode.CONTINUITIES
            }
            headerViewBinder = object : MonthHeaderViewBinder<View>() {
                override fun onBind(view: View, calendarDay: CalendarDay) {
                    val tvHeaderTitle = view.findViewById<TextView>(R.id.tv_header_title)
                    tvHeaderTitle.text = calendarDay.formatDate("yyyy年MM月")
                }

                override fun isStick(): Boolean {
                    return true
                }

                override fun create(parent: ViewGroup): View {
                    return LayoutInflater.from(parent.context).inflate(R.layout.item_header_view, parent, false)
                }
            }
            monthViewBinder = object : MonthViewBinder<MultipleMonthViewSimple> {
                override fun create(parent: ViewGroup): MultipleMonthViewSimple {
                    return MultipleMonthViewSimple(parent.context).apply {
                        dividerHeight = Util.dip2px(parent.context, 10F).toInt()
                    }
                }

                override fun onBind(view: MultipleMonthViewSimple, calendarDay: CalendarDay) {
                    view.selected = this@MultipleSelectActivity.selected
                    view.onMultipleSelectedListener = object : OnMultipleSelectedListener {
                        override fun onSelected(selected: Set<CalendarDay>) {
                            Log.d(TAG, selected.map {
                                it.formatDate("yyyy-MM-dd")
                            }.toString())
                        }
                    }
                }
            }
            //设置月份滚动监听
            monthScrollListener = object : OnMonthScrollListener {
                override fun onScroll(calendarDay: CalendarDay) {
                    Log.d(TAG, calendarDay.toString())
                }
            }
            setUp(calendarConfig)
        }
    }
}