# 仿携程日历控件
RecycleView实现日历列表,其中每个itemView纯cavas绘制。
---
## 支持以下功能
- [x] 日历选中和不可选样式
- [x] 行间距以及颜色设置
- [x] 当选择模式为范围选择时，支持最大可以选择多少天
- [x] 只展示设置的日期范围
- [x] 月份布局是否悬停展示
- [x] 单选和范围选择
- [x] 单选和范围选择自定义样式
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
        android:background="#FAFAFA"
        android:paddingTop="10dp"
        android:paddingBottom="10dp"
        sy:lableArr="@array/month_view_lable_arr"
        sy:lableTextColor="#333333"
        sy:lableTextSize="13sp"
        sy:lableWeekendTextColor="#333333" />
    <!--日历-->
    <com.sunyuan.calendarlibrary.CalendarView
        android:id="@+id/calendar_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#FFFFFF"
        sy:bottomTextSize="10sp"
        sy:disTextColor="#999999"
        sy:dividerColor="#E8E8E8"
        sy:dividerHeight="1px"
        sy:firstSelectDayText="出发"
        sy:firstTopMargin="10dp"
        sy:rowHeight="64dp"
        sy:sameTextColor="#333333"
        sy:secondTopMargin="5dp"
        sy:selectBgDrawable="@drawable/drawable_select_bg"
        sy:selectTextColor="#FFFFFF"
        sy:textColor="#333333"
        sy:textSize="16sp"
        sy:thirdTopMargin="5dp"
        sy:topTextColor="#333333"
        sy:topTextSize="10sp"
        sy:weekendTextColor="#FF6E00" />
````

### XML 自定义属性

MonthLableView(星期)

|属性 | 类型 | 描述 |
| :------------------------- | --------- | ---------------------------------- |
| lableWeekendTextColor | color | 周末字体颜色 |
| lableTextColor | color | 周一到周五字体颜色 |
| lableTextSize | color | 字体大小 |
| lableArr | reference | 星期展示的文案 |

CalendarView(日历)

| 属性 | 类型 | 描述 | 
| :------------------------- | --------- | ---------------------------------- |
| textColor | color | 日历天字体颜色 |
| selectTextColor | color | 选中字体颜色 |
| weekendTextColor | color | 周末字体颜色 |
| disTextColor | color | 不可选字体颜色 |
| sameTextColor | color | 今天字体颜色 |
| topTextColor | color | 节日字体颜色 |
| selectMaxRange | integer | 限制最大可选多少天 |
| monthPaddingLeft | dimension | 月份paddingLeft |
| monthPaddingTop | dimension | 月份paddingTop |
| monthPaddingRight | dimension | 月份paddingRight |
| monthPaddingBottom | dimension | 月份paddingBottom |
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
| selectBgDrawable| reference | 选中样式 |
| selectRangeBgDrawable | reference | 选中范围样式 |


### 代码
````java
  CalendarViewWrapper.wrap(calendarView)
                //设置展示的日期范围
                .setDateRange(minDate, maxDate)
                //设置默认选中的日期
                .setCalendarSelectDay(calendarSelectDay)
                //选中模式-单选
                .setSelectionMode(SelectionMode.SINGLE)
                //设置日历选中事件
                .setOnCalendarSelectDayListener(new OnCalendarSelectDayListener<CalendarDay>() {
                    @Override
                    public void onCalendarSelectDay(CalendarSelectDay<CalendarDay> calendarSelectDay) {
                        CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
                        if (firstSelectDay != null) {
                            String firstSelectDateStr = formatDate("yyyy-MM-dd", firstSelectDay.toDate());
                            tvCurrentSelectDate.setText(firstSelectDateStr);
                        }
                    }
                })
                //月份头是否悬停
                .setStick(false)
                //是否展示月份布局
                .setShowMonthTitleView(true)
                //设置月份布局回调
                .setMonthTitleViewCallBack(new MonthTitleViewCallBack() {
                    @Override
                    public View getMonthTitleView(int position, Date date) {
                        View view = View.inflate(SingleSelectActivity.this, R.layout.layout_month_title, null);
                        TextView tvMonthTitle = view.findViewById(R.id.tv_month_title);
                        tvMonthTitle.setText(formatDate("yyyy年MM月", date));
                        return view;
                    }
                })
                .display();
        //根据指定日期得到position位置
        int position = calendarView.covertToPosition(calendarSelectDay.getFirstSelectDay());
        if (position != -1) {
            //滚动到指定位置
            calendarView.smoothScrollToPosition(position);
        }
````
