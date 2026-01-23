package com.sy007.calendar.widget.help

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
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
        if (state.isPreLayout) return
        val childCount = parent.childCount
        if (childCount == 0) return
        for (i in 0 until childCount) {
            if (i != 0) {
                continue
            }
            c.save()
            val curItemView = parent.getChildAt(i)
            val curItemPos = parent.getChildAdapterPosition(curItemView)
            if (curItemPos == RecyclerView.NO_POSITION) {
                c.restore()
                continue
            }
            val curItemHolder = parent.findViewHolderForLayoutPosition(curItemPos) as? MonthViewHolder
            val curHeaderView = curItemHolder?.headerView
            if (curHeaderView == null) {
                c.restore()
                continue
            }
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
                    parent.paddingTop,
                    parent.paddingLeft + headerViewWidth,
                    parent.paddingTop + offset
                )
            } else {
                // 处理下一个Item的逻辑（修复小屏幕问题）
                val nextItemView = parent.getChildAt(1)
                val nextItemPos = parent.getChildAdapterPosition(nextItemView)
                // 最终要绘制的HeaderView（兜底逻辑）
                val drawView: View = if (nextItemPos != RecyclerView.NO_POSITION && (nextItemView?.top ?: Int.MAX_VALUE) <= parent.paddingTop) {
                        // 下一个Item有效且需要切换吸顶Header
                        val nextHolder = parent.findViewHolderForLayoutPosition(nextItemPos) as? MonthViewHolder
                        stickHeaderPos = nextItemPos
                        nextHolder?.headerView ?: curHeaderView
                    } else {
                        // 下一个Item无效/不存在，使用当前Header
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

