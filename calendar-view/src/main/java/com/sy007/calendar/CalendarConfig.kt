package com.sy007.calendar

import androidx.recyclerview.widget.RecyclerView
import java.util.*


/**
 * 日历配置类
 * <p>其用于日历的显示和操作模式
 * @property startCalendar 日历展示的开始日期
 * @property endCalendar   日历展示的结束日期
 *
 * @author sy007
 * @date 2022/4/9
 */
class CalendarConfig constructor(val startCalendar: Calendar, val endCalendar: Calendar) {

    /**
     * 日历展示方向
     *
     * <p>属性值传递[RecyclerView.VERTICAL]或[RecyclerView.HORIZONTAL]
     */
    @RecyclerView.Orientation
    var orientation = RecyclerView.VERTICAL

    /**
     * 月份展示从星期几开始，默认是星期日
     *
     * @see WeekStart
     */
    var weekStart: WeekStart = WeekStart.SUNDAY

    /**
     * 月份视图高度模式
     *
     * <p>当属性值为[HeightMode.DYNAMIC]时月份视图的高度由程序动态计算，一般为5行或者6行高度
     * <p>当属性值为[HeightMode.FIXED]时月份视图的高度为固定高度，月份视图固定展示6行高度
     * <p>当构造参数[startCalendar]的日期不是该月份的第一天或者[endCalendar]的日期不是该月份的最后一天，设置的[HeightMode.FIXED]模式会被修正为[HeightMode.DYNAMIC]模式
     */
    var heightMode: HeightMode = HeightMode.DYNAMIC

    /**
     * 是否绘制额外的日期
     *
     * <p>当属性值为true时，月份视图上的额外空间将绘制小于[startCalendar]和大于[endCalendar]的日期；如果不够填充，那么会填充前一个月和后一个月的日期
     * <p>当属性值为false时，月份视图上的额外空间不进行绘制
     */
    var isDisplayExtraDay: Boolean = false


    /**
     * 滑动模式
     *
     * <p>当属性值为[ScrollMode.PAGE]时，日历将在滑动操作后自动滑动到最近的月份，形如ViewPager滑动效果
     * <p>当属性值为[ScrollMode.CONTINUITIES]时，日历正常滑动
     */
    var scrollMode = ScrollMode.PAGE
}

/**
 * 月份视图的高度模式
 */
enum class HeightMode {
    /**
     * 月份视图的高度由程序动态计算，一般为5行或者6行高度
     */
    DYNAMIC,

    /**
     * 月份视图固定展示6行高度
     */
    FIXED
}

/**
 * 日历滚动模式
 */
enum class ScrollMode {
    /**
     * 日历正常滑动
     */
    CONTINUITIES,

    /**
     *日历将在滑动操作后自动滑动到最近的月份，形如ViewPager滑动效果
     */
    PAGE
}

/**
 * 月份展示从星期几开始
 */
enum class WeekStart constructor(val value: Int) {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);
}

