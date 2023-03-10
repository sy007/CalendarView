# 自定义日历控件
RecycleView实现,每个itemView cavas绘制，这意味着日历上每个月份很容易的实现自定义绘制。

### 1. 支持以下功能

- [x] 日历支持自定义范围展示
- [x] 月份视图支持自定义行高度,间距,间距颜色设置
- [x] 月份视图支持自定义绘制
- [x] 月份视图头，脚布局。头布局支持悬停
- [x] 月份视图支持固定行和动态行高度展示 (固定行:6行，动态行:根据当前展示的月份计算)
- [x] 月份视图支持从一周的某个星期开始
- [x] 月份视图支持纵向，横向滑动(ViewPager模式)
- [x] 日期支持单选，范围选择，多选
- [x] 日期支持点击事件拦截
- [x] 日期支持自定义绘制
### 2. Example

![image](http://m.qpic.cn/psc?/V11vVsP84HfNn2/bqQfVz5yrrGYSXMvKr.cqfZqDZTE14QcuJmw9w*x3uW9sPUlZ5R7gG4UkZq4hYu95iD96W3.z26xo0p9OlPMGCAQiIbNPdfscbFf50GGr20!/b&bo=cBfGCnAXxgoBByA!&rf=viewer_4)
### 2. 集成

```groovy
implementation 'io.github.sy007:calendar-view:1.2.0'
```

### 3. 使用

#### 3.1. 在XML中定义CalendarView 

```xml
<!--周视图-->
<com.sy007.calendar.widget.WeekView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="10dp"
    android:layout_marginBottom="10dp"
    sy:wv_content="@array/week_sunday_to_saturday"
    sy:wv_text_color="#333333"
    sy:wv_text_size="13sp" />

<!--日历-->
<com.sy007.calendar.widget.CalendarView
    android:id="@+id/cv_single_calendar_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

#### 3.2. 代码中初始化

##### 3.2.1. 设置月份布局

```kotlin
var selectedDay:CalendarDay?=null
val cvSingleCalendarView:CalendarView
...
//设置月份视图
cvSingleCalendarView.monthViewBinder = object : MonthViewBinder<SingleMonthViewSimple2> {
    override fun create(parent: ViewGroup): SingleMonthViewSimple2 {
        return SingleMonthViewSimple2(parent.context)
    }
    override fun onBind(view: SingleMonthViewSimple2, calendarDay: CalendarDay) {
        view.apply {
            //设置选中的日期
            selected = selectedDay
            onSelectedListener = object : OnSelectedListener {
                override fun onSelected(selected: CalendarDay) {
                    selectedDay = selected
                    //点击日期后滑动到指定日期
                    cvSingleCalendarView.smoothScrollToMonth(selected)
                    tvCurrentSelectedDate.text = selected.formatDate("yyyy-MM-dd")
                }
            }
        }
    }
}
```

##### 3.2.2. 构建日历展示范围和样式数据模型

```kotlin
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
```

##### 3.2.3. 将数据模型设置给日历视图

```kotlin
cvSingleCalendarView.setUp(calendarConfig)
```

`SingleMonthViewSimple2`是自定义月份视图，它继承SingleMonthView；你可以在`SingleMonthViewSimple2`中自定义绘制和处理日期是否可以被选中逻辑。

框架内部还内置了`RangeMonthView`(范围选择),`MultipleMonthView`（多选）月份视图基类，根据需求选择一个继承即可。

SingleMonthViewSimple2简略代码如下

```kotlin
class SingleMonthViewSimple2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SingleMonthView(context, attrs, defStyleAttr) {

    /**
     * 拦截日期选中事件，如果拦截则不会回调日期选中事件
     * 你可以在此方法中拦截不想让用户点击的日期
     * @return true 表示拦截，false 表示不拦截
     */
    override fun onInterceptSelect(calendarDay: CalendarDay): Boolean {
        return super.onInterceptSelect(calendarDay)
    }

    /**
     * 月份日期绘制前调用，你可以在绘制前做一些事
     */
    override fun onDrawMonthBefore(canvas: Canvas, calendarDay: CalendarDay) {
        super.onDrawMonthBefore(canvas, calendarDay)

    }

    /**
     * 月份日期绘制后调用，你可以在绘制后做一些事
     */
    override fun onDrawMonthAfter(canvas: Canvas, calendarDay: CalendarDay) {
        super.onDrawMonthAfter(canvas, calendarDay)
    }

    /**
     * 日期绘制时调用，你可以在这里绘制月份上的日期
     *
     * [calendarDay] 待绘制的日期模型
     * [rect]       日期绘制的范围
     * [isSelected] 该日期是否是选中状态
     */
    override fun onDrawDay(canvas: Canvas, calendarDay: CalendarDay, rect: Rect, isSelected: Boolean) {

    }
}
```

### 4. 自定义属性

WeekView(周视图)

|属性 | 类型 | 描述 |
| :------------------------- | --------- | ---------------------------------- |
| wv_weekend_text_color | color | 周末字体颜色 |
| wv_text_color | color | 默认字体颜色 |
| wv_text_size | color | 字体大小 |
| wv_content | reference | 星期展示的文案 |
| wv_weekend_index | reference | 周末索引(如果要设置周末字体颜色,需要告诉框架周末在wv_content中的索引位置) |

XXMonthView(月份视图)

| 属性 | 类型 | 描述 |
| :------------------------- | --------- | ---------------------------------- |
| dividerHeight | dimension | 月份视图行间距高度 |
| dividerColor | color | 月份视图行间距颜色 |
| rowHeight | dimension | 月份视图行高度 |

### 5. [Docs](https://sy007.github.io/calendar-view-docs/html/)

### 6. 更新日志

#### v1.2.1

修复`ScrollMode.PAGE`模式下,高度无法自适应问题

#### v1.2.0

支持脚布局

#### v1.0.1

修复获取`BaseMonthView#rowHeight`自定义属性类型不正确问题





