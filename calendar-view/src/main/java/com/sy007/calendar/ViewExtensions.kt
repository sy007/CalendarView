package com.sy007.calendar

import android.view.View
import android.view.ViewGroup
import com.sy007.calendar.widget.BaseMonthView

/**
 * @author sy007
 * @date 2022/5/1
 */
internal fun View?.lp(): ViewGroup.MarginLayoutParams? {
    if (this == null) {
        return null
    }
    return layoutParams as? ViewGroup.MarginLayoutParams
}

internal fun View?.getVerticalMargins(): Int {
    val lp = lp()
    return if (lp == null) {
        0
    } else {
        lp.topMargin + lp.bottomMargin
    }
}
