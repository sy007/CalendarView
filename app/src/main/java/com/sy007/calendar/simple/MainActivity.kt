package com.sy007.calendar.simple

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sy007.calendar.simple.databinding.ActivityMainBinding
import com.sy007.calendar.simple.multiple.MultipleSelectActivity
import com.sy007.calendar.simple.range.RangeSelectActivity
import com.sy007.calendar.simple.single.SingleHorizontalActivity
import com.sy007.calendar.simple.single.SingleVerticalSelectActivity

const val SIMPLE_1 = "simple1"
const val SIMPLE_2 = "simple2"
const val SIMPLE_3 = "simple3"
const val SIMPLE_4 = "simple4"

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setTitle("CalendarView")
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = SimpleAdapter(mutableListOf<SimpleData>().apply {
                add(SimpleData("单选", "展示纵向滑动的月份视图", SIMPLE_1))
                add(SimpleData("单选", "展示横向滑动展示农历的月份视图(ViewPager滑动模式)", SIMPLE_2))
                add(SimpleData("范围选择", "展示可以选择连续日期的月份视图，支持设置可连续的天数", SIMPLE_3))
                add(SimpleData("多选", "展示可以选择多个不连续日期的月份视图", SIMPLE_4))
            }) {
                when (it.tag as String) {
                    SIMPLE_1 -> {
                        SingleVerticalSelectActivity.start(this@MainActivity)
                    }
                    SIMPLE_2 -> {
                        SingleHorizontalActivity.start(this@MainActivity)
                    }
                    SIMPLE_3 -> {
                        RangeSelectActivity.start(this@MainActivity)
                    }
                    SIMPLE_4 -> {
                        MultipleSelectActivity.start(this@MainActivity)
                    }
                }
            }
        }
    }
}