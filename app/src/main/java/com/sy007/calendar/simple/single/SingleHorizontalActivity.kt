package com.sy007.calendar.simple.single

import android.annotation.SuppressLint
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
import com.sy007.calendar.databinding.ActivitySingleHorizontalBinding
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.simple.BaseActivity
import com.sy007.calendar.simple.utils.LunarCalendar
import java.util.*

class SingleHorizontalActivity : BaseActivity() {

    private var selectedDay: CalendarDay? = null
    private lateinit var binding: ActivitySingleHorizontalBinding
    private lateinit var tvCurrentSelectedDate: TextView

    companion object {
        private const val TAG = "SingleHorizontalActivity"
        fun start(context: Context) {
            val intent = Intent(context, SingleHorizontalActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleHorizontalBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        tvCurrentSelectedDate = binding.layoutCurrentSingleSelectContainer.tvCurrentSelectedDate
        setTitle("单选横向滑动(ViewPager滑动模式)")
        //初始化节日
        LunarCalendar.init(this@SingleHorizontalActivity)
        binding.apply {
            cvSingleCalendarView.apply {
                val startCalendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, getActualMinimum(Calendar.DAY_OF_MONTH))
                }
                val endCalendar = Calendar.getInstance().apply {
                    add(Calendar.MONTH, 10)
                    set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
                }
                //初始化日历展示范围和样式
                val calendarConfig = CalendarConfig(startCalendar, endCalendar).apply {
                    //横向滑动
                    orientation = RecyclerView.HORIZONTAL
                    //ViewPager滚动模式
                    scrollMode = ScrollMode.PAGE
                    //固定6行高度
                    heightMode = HeightMode.FIXED
                    //月份视图上展示额外日期
                    isDisplayExtraDay = true
                }
                headerViewBinder = object : MonthHeaderViewBinder<View>() {
                    override fun create(parent: ViewGroup): View {
                        return LayoutInflater.from(parent.context).inflate(
                            R.layout.item_header_view,
                            parent,
                            false
                        )
                    }

                    override fun onBind(view: View, calendarDay: CalendarDay) {
                        view.findViewById<TextView>(R.id.tv_header_title).text =
                            "header:${calendarDay.formatDate("yyyy-MM-dd")}"
                    }
                }
                monthViewBinder = object : MonthViewBinder<SingleMonthViewSimple2> {
                    override fun create(parent: ViewGroup): SingleMonthViewSimple2 {
                        return LayoutInflater.from(parent.context).inflate(
                            R.layout.layout_single_month_view_simple2,
                            parent,
                            false
                        ) as SingleMonthViewSimple2
                    }

                    override fun onBind(view: SingleMonthViewSimple2, calendarDay: CalendarDay) {
                        view.apply {
                            //设置选中的日期
                            selected = selectedDay
                            onSelectedListener = object : OnSelectedListener {
                                override fun onSelected(selected: CalendarDay) {
                                    selectedDay = selected
                                    //点击日期后滑动到指定日期
                                    binding.cvSingleCalendarView.smoothScrollToMonth(selected)
                                    tvCurrentSelectedDate.text = selected.formatDate("yyyy-MM-dd")
                                }
                            }
                        }
                    }
                }
                footerViewBinder = object : MonthFooterViewBinder<View> {
                    override fun create(parent: ViewGroup): View {
                        return LayoutInflater.from(parent.context).inflate(
                            R.layout.item_footer_view,
                            parent,
                            false
                        )
                    }

                    override fun onBind(view: View, calendarDay: CalendarDay) {
                        view.findViewById<TextView>(R.id.tv_footer_title).text =
                            "footer:${calendarDay.formatDate("yyyy-MM-dd")}"
                    }
                }
                //监听月份滚动监听
                monthScrollListener = object : OnMonthScrollListener {
                    @SuppressLint("LongLogTag")
                    override fun onScroll(calendarDay: CalendarDay) {
                        Log.d(TAG, calendarDay.toString())
                    }
                }
                //设置数据
                setUp(calendarConfig)
            }
        }
    }
}