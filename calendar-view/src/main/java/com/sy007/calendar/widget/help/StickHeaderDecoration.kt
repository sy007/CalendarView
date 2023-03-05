package com.sy007.calendar.widget.help

import android.graphics.Canvas
import android.graphics.Rect
import android.util.Half.toFloat
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * @author sy007
 * @date 2019/01/20
 */
internal class StickHeaderDecoration : ItemDecoration() {
    val stickHeaderRect = Rect()
    var stickHeaderPos = RecyclerView.NO_POSITION
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (i != 0) {
                continue
            }
            c.save()
            val curItemView = parent.getChildAt(i)
            val curItemPos = parent.getChildAdapterPosition(curItemView)
            val curItemHolder =
                parent.findViewHolderForLayoutPosition(curItemPos) as MonthViewHolder
            val curHeaderView = curItemHolder.headerView ?: return
            val headerViewHeight = curHeaderView.height
            val headerViewWidth = curHeaderView.width
            val offset = curItemView.bottom - parent.paddingTop
            if (offset <= headerViewHeight && curItemView.bottom >= parent.paddingTop) {
                stickHeaderPos = curItemPos
                c.translate(
                    parent.paddingLeft.toFloat(),
                    parent.paddingTop + (offset - headerViewHeight).toFloat()
                )
                c.clipRect(
                    0,
                    -(offset - headerViewHeight),
                    headerViewWidth,
                    headerViewHeight
                )
                curHeaderView.draw(c)
                c.restore()
                stickHeaderRect.set(
                    parent.paddingLeft,
                    parent.paddingTop, parent.paddingLeft + headerViewWidth,
                    parent.paddingTop + offset
                )
            } else {
                val nextItemView = parent.getChildAt(1)
                val nextItemPos = parent.getChildAdapterPosition(nextItemView)
                val nextHolder =
                    parent.findViewHolderForLayoutPosition(nextItemPos) as MonthViewHolder
                val nextHeaderView = nextHolder.headerView
                val drawView = if (nextItemView.top <= parent.paddingTop) {
                    stickHeaderPos = nextItemPos
                    nextHeaderView
                } else {
                    stickHeaderPos = curItemPos
                    curHeaderView
                }
                c.translate(parent.paddingLeft.toFloat(), parent.paddingTop.toFloat())
                c.clipRect(0, 0, headerViewWidth, headerViewHeight)
                drawView.draw(c)
                c.restore()
                stickHeaderRect.set(
                    parent.paddingLeft,
                    parent.paddingTop,
                    parent.paddingLeft + headerViewWidth,
                    parent.paddingTop + headerViewHeight
                )
            }
        }
    }
}