package com.sy007.calendar.simple.range

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.sy007.calendar.*
import com.sy007.calendar.entity.*
import com.sy007.calendar.simple.BaseActivity
import com.sy007.calendar.R
import com.sy007.calendar.databinding.ActivityRangeSelectBinding
import com.sy007.calendar.databinding.DialogInputMaximumDayBinding
import com.sy007.calendar.simple.utils.LunarCalendar
import com.sy007.calendar.utils.Util
import java.util.*

class RangeSelectActivity : BaseActivity() {
    private var maximumDay = 10

    companion object {
        const val TAG = "RangeSelectActivity"
        fun start(context: Context) {
            val intent = Intent(context, RangeSelectActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityRangeSelectBinding
    private var selected: CalendarRangeSelected = CalendarRangeSelected()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRangeSelectBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        //初始化节日
        LunarCalendar.init(this)
        initView()
    }

    private fun initView() {
        setTitle("范围选择")
        binding.apply {
            btnMaximumDay.setOnClickListener {
                showInputMaximumDayDialog()
            }
            cvRangeCalendarView.apply {
                headerViewBinder = object : MonthHeaderViewBinder<View>() {
                    override fun onBind(view: View, calendarDay: CalendarDay) {
                        val tvMonthTitle = view.findViewById<TextView>(R.id.tv_month_title)
                        tvMonthTitle.text = calendarDay.formatDate("yyyy年MM月")
                    }

                    override fun isStick(): Boolean {
                        return true
                    }

                    override fun create(parent: ViewGroup): View {
                        return LayoutInflater.from(parent.context).inflate(R.layout.layout_calendar_month_title, parent, false)
                    }
                }
                monthViewBinder = object : MonthViewBinder<RangeMonthViewSimple> {
                    override fun create(parent: ViewGroup): RangeMonthViewSimple {
                        return RangeMonthViewSimple(parent.context).apply {
                            dividerHeight = Util.dip2px(parent.context, 10F).toInt()
                        }
                    }

                    override fun onBind(view: RangeMonthViewSimple, calendarDay: CalendarDay) {
                        view.selected = selected
                        view.maximumDay = this@RangeSelectActivity.maximumDay
                        view.onSelectedListener = object : OnRangeSelectedListener {
                            override fun onSelected(selected: CalendarRangeSelected) {
                                selected.apply {
                                    binding.tvFirstSelectedDate.text = firstSelected?.formatDate("yyyy-MM-dd")
                                    binding.tvLastSelectedDate.text = lastSelected?.formatDate("yyyy-MM-dd")
                                }
                            }
                        }
                    }
                }
                monthScrollListener = object : OnMonthScrollListener {
                    override fun onScroll(calendarDay: CalendarDay) {
                        Log.e(TAG, calendarDay.toString())
                    }
                }
                val minCalendar = Calendar.getInstance()
                val maxCalendar = Calendar.getInstance()
                minCalendar.set(Calendar.DAY_OF_MONTH, minCalendar.getActualMinimum(Calendar.DAY_OF_MONTH))
                maxCalendar.add(Calendar.MONTH, 10)
                maxCalendar.set(Calendar.DAY_OF_MONTH, maxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                val calendarConfig = CalendarConfig(minCalendar, maxCalendar).apply {
                    scrollMode = ScrollMode.CONTINUITIES
                }
                setUp(calendarConfig)
            }
        }
    }

    private fun showInputMaximumDayDialog() {
        DialogInputMaximumDayBinding.inflate(LayoutInflater.from(this)).apply {
            val dialog = Dialog(this@RangeSelectActivity).apply {
                setContentView(root)
                show()
                (resources.displayMetrics.widthPixels * 0.8F).also {
                    window?.attributes = window?.attributes?.apply {
                        width = it.toInt()
                    }
                }
            }
            etInputMaximumDay.setText(maximumDay.toString())
            btnConfirm.setOnClickListener {
                if (etInputMaximumDay.text.toString().isEmpty()) {
                    Toast.makeText(this@RangeSelectActivity, "请输入最大可选择的天数", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                dialog.dismiss()
                binding.btnMaximumDay.text = "最多可选择${etInputMaximumDay.text}天"
                maximumDay = etInputMaximumDay.text.toString().toInt()
                binding.cvRangeCalendarView.notifyCalendarChanged()
            }
        }
    }
}