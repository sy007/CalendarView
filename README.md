# 仿携程日历控件
RecycleView实现日历列表,其中每个itemView纯cavas绘制。
---
## 支持以下功能
- [x] 日历选中和不可选样式
- [x] 行间距以及颜色设置
- [x] 最大可以选择多少天
- [x] 悬停月份展示
- [x] 单选和范围选择
## Demo
### 单选
![image](https://ws1.sinaimg.cn/large/006xnoHVly1fzl6io7cnhg30b40m8qth.gif)
### 范围选择
![image](https://i.loli.net/2019/01/26/5c4c547a69718.gif)
## 使用
### XML文件
````xml

<!--星期-->
<com.sunyuan.calendarlibrary.MonthLableView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="30dp" />
<!--日历-->
<com.sunyuan.calendarlibrary.CalendarView
    android:id="@+id/calendar_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="#FFFFFF"
    sy:dividerHeight="5dp"
    sy:firstSelectDayText="入住"
    sy:lastSelectDayText="离店"
    sy:selectMaxRange="28" />
````

### XML 自定义属性

MonthLableView(星期)

|属性 | 类型 | 描述 |
| :------------------------- | --------- | ---------------------------------- |
| lableWeekendTextColor | color | 周末字体颜色 |
| lableTextColor | color | 周一到周五字体颜色 |
| lableTextSize | color | 字体大小 |

CalendarView(日历)

| 属性 | 类型 | 描述 | 
| :------------------------- | --------- | ---------------------------------- |
| isShowMonthTitleView | boolean | 是否显示MonthTitleView默认显示 |
| textColor | color | 日历天字体颜色 |
| selectTextColor | color | 选中字体颜色 |
| weekendTextColor | color | 周末字体颜色 |
| selectBgColor | color | 选中背景颜色 |
| disTextColor | color | 不可选字体颜色 |
| sameTextColor | color | 今天字体颜色 |
| preTextColor | color | 已过期字体颜色 |
| topTextColor | color | 节日字体颜色 |
| selectRangebgColor | color | 选中间隔背景颜色 |
| selectMaxRange | integer | 限制最大可选多少天 |
| monthPaddingLeft | dimension | 月份paddingLeft |
| monthPaddingTop | dimension | 月份paddingTop |
| monthPaddingRight | dimension | 月份paddingRight |
| monthPaddingBottom | dimension | 月份paddingBottom |
| monthMarginLeft | dimension | 月份MarginLeft |
| monthMarginTop | dimension | 月份MarginTop |
| monthMarginRight | dimension | 月份MarginRight |
| monthMarginBottom | dimension | 月份MarginBottom |
|textSize | dimension | 日历天字体大小 |
| topTextSize | dimension | 节日字体大小 |
| bottomTextSize | dimension | 选中底部文案字体大小 |
| firstTopMargin | dimension | 节日距离顶部距离 |
| secondTopMargin | dimension | 天距离节日底部距离 |
| thirdTopMargin | dimension | 选中文案距离天底部距离 |
| dividerHeight | dimension | 日历每一行间距 |
| dividerColor | color | 日历每一行间距颜色 |
| firstSelectDayText | string | 第一次选中底部文案 |
| lastSelectDayText | string | 最后一次选中底部文案 |
| selectStyle | enum | 选中样式(rectangle 矩形  roundRectangle 圆角矩形) |
| cornerRadius | dimension | 当选中为圆角矩形时圆角半径 |
| isSingleSelect | boolean | 是否单选默认为范围选择 |

### 代码
````java
//设置初始化选中日期
calendarView.setCalendarSelectDay(calendarSelectDay);
//绘制monthTitleView 当isShowMonthTitleView为false时不需要设置
calendarView.setMonthTitleViewCallBack(new MonthTitleViewCallBack() {
    @Override
    public View getMonthTitleView(int position, Date date) {
        View view = View.inflate(MainActivity.this, R.layout.layout_month_title, null);
        TextView tvMonthTitle = view.findViewById(R.id.tv_month_title);
        tvMonthTitle.setText(formatDate("yyyy年MM月", date));
        return view;
    }
});
//设置选中事件
calendarView.setOnCalendarSelectDayListener(new CalendarView.OnCalendarSelectDayListener<CalendarDay>() {
    @Override
    public void onCalendarSelectDay(CalendarSelectDay<CalendarDay> calendarSelectDay) {
        CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
        CalendarDay lastSelectDay = calendarSelectDay.getLastSelectDay();
        if (firstSelectDay != null) {
            String firstSelectDateStr = formatDate("yyyy-MM-dd", firstSelectDay.toDate());
            tvFirstSelectDate.setText(firstSelectDateStr);
        }
        if (lastSelectDay != null) {
            String lastSelectDateStr = formatDate("yyyy-MM-dd", lastSelectDay.toDate());
            tvLastSelectDate.setText(lastSelectDateStr);
        }
    }
});
````
