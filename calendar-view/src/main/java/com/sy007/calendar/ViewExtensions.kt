package com.sy007.calendar

import android.view.View
import android.view.ViewGroup

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

internal fun View.ifNullCreateLp(): ViewGroup.LayoutParams {
    var lp = layoutParams
    if (lp == null) {
        lp = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    return lp
}

internal fun View?.getVerticalSpace(): Int {
    return this?.run {
        height + getVerticalMargins()
    } ?: 0
}

internal fun View?.getVerticalMargins(): Int {
    val lp = lp()
    return if (lp == null) {
        0
    } else {
        lp.topMargin + lp.bottomMargin
    }
}
