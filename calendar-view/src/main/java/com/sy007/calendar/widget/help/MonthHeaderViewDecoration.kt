package com.sy007.calendar.widget.help

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.sy007.calendar.entity.CalendarDay
import com.sy007.calendar.MonthHeaderViewBinder
import java.util.*

/**
 * @author sy007
 * @date 2019/01/20
 */
internal class MonthHeaderViewDecoration(private val headerViewBinder: MonthHeaderViewBinder<View>) : ItemDecoration() {

    private val measuredCache = WeakHashMap<Int, View?>()

    fun getHeaderViewForPosition(position: Int): View? {
        return measuredCache[position]
    }

    interface CalendarDayCallback {
        fun getCalendarDayByPosition(position: Int): CalendarDay
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        if (adapter is CalendarDayCallback) {
            val calendarDayCallback = adapter as CalendarDayCallback
            val position = parent.getChildAdapterPosition(view)
            val headerView: View?
            if (measuredCache[position] == null) {
                val calendarDay = calendarDayCallback.getCalendarDayByPosition(position)
                headerView = headerViewBinder.create(parent)
                headerViewBinder.onBind(headerView, calendarDay)
                measureAndLayoutHeaderView(headerView, parent)
                measuredCache[position] = headerView
            } else {
                headerView = measuredCache[position]
            }
            //预留HeaderView的高度
            outRect.top = headerView?.height ?: 0
        }
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        if (adapter is CalendarDayCallback) {
            val childCount = parent.childCount
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            var top = parent.paddingTop
            /**
             * 粘性思路
             *
             * 1.当最后一个itemView bottom与headerView bottom重合时候。第二个headerView就到了第一个headerView底部
             * 2.当重合后再往上滑动时，只需要不断改变悬停headerView top值(itemView.bottom-headerViewHeight)重新绘制
             * 从而视觉上感觉 第二个headerView往上推第一个headerView。
             */
            for (i in 0 until childCount) {
                val view = parent.getChildAt(i)
                val position = parent.getChildAdapterPosition(view)
                val headerView = measuredCache[position] ?: return
                val headerViewHeight = headerView.height
                if (headerViewBinder.isStick()) {
                    if (i == 0) {
                        val tempTop = view.bottom - headerViewHeight
                        if (tempTop < top) {
                            top = tempTop
                        }
                    } else {
                        top = view.top - headerViewHeight
                    }
                } else {
                    top = view.top - headerViewHeight
                }
                c.save()
                c.translate(0f, top.toFloat())
                c.clipRect(left, 0, right, headerViewHeight)
                headerView.draw(c)
                c.restore()
            }
        }
    }

    private fun measureAndLayoutHeaderView(headerView: View, parent: RecyclerView) {
        if (headerView.layoutParams == null) {
            headerView.layoutParams = MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
        }
        val lp = headerView.layoutParams as MarginLayoutParams
        val parentWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.measuredWidth, View.MeasureSpec.EXACTLY)
        val widthSpec = ViewGroup.getChildMeasureSpec(parentWidthSpec, parent.paddingLeft + parent.paddingRight +
                lp.leftMargin + lp.rightMargin, lp.width)
        val heightSpec = if (lp.height > 0) {
            View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY)
        } else {
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        }
        headerView.measure(widthSpec, heightSpec)
        headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)
    }
}