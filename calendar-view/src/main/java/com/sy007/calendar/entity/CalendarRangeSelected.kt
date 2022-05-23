package com.sy007.calendar.entity

import java.io.Serializable

/**
 * 日期范围选择模型类
 * @author sy007
 * @date 2019/01/20
 */
class CalendarRangeSelected : Serializable {
    var firstSelected: CalendarDay? = null
    var lastSelected: CalendarDay? = null
}