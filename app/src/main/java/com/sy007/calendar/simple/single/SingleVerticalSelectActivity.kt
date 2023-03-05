package com.sy007.calendar.simple.single

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.*
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.widget.CalendarView
import com.sy007.calendar.R
import com.sy007.calendar.databinding.ActivitySingleSelectBinding
import com.sy007.calendar.simple.BaseActivity
import com.sy007.calendar.utils.Util
import java.util.*

class SingleVerticalSelectActivity : BaseActivity() {
    private var selectedDay: CalendarDay? = null
    private lateinit var binding: ActivitySingleSelectBinding
    private lateinit var tvCurrentSelectedDate: TextView
    private var disableCalendarDays = mutableSetOf<CalendarDay>()

    companion object {
        private const val TAG = "SingleSelectActivity"
        fun start(context: Context) {
            val intent = Intent(context, SingleVerticalSelectActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleSelectBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        tvCurrentSelectedDate = binding.layoutCurrentSingleSelectContainer.tvCurrentSelectedDate
        setTitle("单选纵向滑动")
        binding.apply {
            btnCalendarDisable.setOnClickListener {
                showDatePickerDialog(Calendar.getInstance()) {
                    disableCalendarDays.add(CalendarDay(it))
                    cvSingleCalendarView.notifyCalendarChanged()
                }
            }
            cvSingleCalendarView.configurationCalendarView()
        }
    }

    private fun CalendarView.configurationCalendarView() {
        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.MONTH, 10)
        val calendarConfig = CalendarConfig(startCalendar, endCalendar).apply {
            orientation = RecyclerView.VERTICAL
            scrollMode = ScrollMode.CONTINUITIES
        }
        headerViewBinder = object : MonthHeaderViewBinder<View>() {
            override fun onBind(view: View, calendarDay: CalendarDay) {
                view.setOnClickListener {
                    Toast.makeText(
                        this@SingleVerticalSelectActivity,
                        "header:${calendarDay.formatDate("yyyy年MM月")}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val tvHeaderTitle = view.findViewById<TextView>(R.id.tv_header_title)
                tvHeaderTitle.text = "header:${calendarDay.formatDate("yyyy年MM月")}"
            }

            override fun isStick(): Boolean {
                return true
            }

            override fun create(parent: ViewGroup): View {
                return LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header_view, parent, false)
            }
        }
        monthViewBinder = object : MonthViewBinder<SingleMonthViewSimple1> {
            override fun create(parent: ViewGroup): SingleMonthViewSimple1 {
                return SingleMonthViewSimple1(parent.context).apply {
                    dividerHeight = Util.dip2px(parent.context, 10F).toInt()
                }
            }

            override fun onBind(view: SingleMonthViewSimple1, calendarDay: CalendarDay) {
                view.selected = selectedDay
                view.disableCalendarDays = this@SingleVerticalSelectActivity.disableCalendarDays
                view.onSelectedListener = object : OnSelectedListener {
                    override fun onSelected(selected: CalendarDay) {
                        this@SingleVerticalSelectActivity.selectedDay = selected
                        tvCurrentSelectedDate.text = selected.formatDate("yyyy-MM-dd")
                        scrollToDay(selected)
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

    private fun showDatePickerDialog(calendar: Calendar, block: (calendar: Calendar) -> Unit) {
        val dialog = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                val cl = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                block(cl)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }
}

