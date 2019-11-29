package com.sunyuan.calendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sunyuan.calendarlibrary.CalendarView;
import com.sunyuan.calendarlibrary.CalendarViewWrapper;
import com.sunyuan.calendarlibrary.MonthTitleViewCallBack;
import com.sunyuan.calendarlibrary.OnCalendarSelectDayListener;
import com.sunyuan.calendarlibrary.SelectionMode;
import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.model.CalendarSelectDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RangeSelectActivity extends AppCompatActivity {
    private TextView tvFirstSelectDate;
    private TextView tvLastSelectDate;
    private CalendarSelectDay<CalendarDay> calendarSelectDay;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_select);
        initSelectCalendar();
        initView();
    }

    private void initView() {
        calendarView = findViewById(R.id.calendar_view);
        tvFirstSelectDate = findViewById(R.id.tv_first_select_date);
        tvLastSelectDate = findViewById(R.id.tv_last_select_date);
        String firstSelectDateStr = formatDate("yyyy-MM-dd", calendarSelectDay.getFirstSelectDay().toDate());
        String lastSelectDateStr = formatDate("yyyy-MM-dd", calendarSelectDay.getLastSelectDay().toDate());
        tvFirstSelectDate.setText(firstSelectDateStr);
        tvLastSelectDate.setText(lastSelectDateStr);

        Calendar calendar = Calendar.getInstance();
        Date minDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 12);
        Date maxDate = calendar.getTime();
        CalendarViewWrapper.wrap(calendarView)
                //设置最大最小日期范围 展示三个月数据
                .setDateRange(minDate, maxDate)
                //设置默认选中日期
                .setCalendarSelectDay(calendarSelectDay)
                //设置选择模式为范围选择
                .setSelectionMode(SelectionMode.RANGE)
                //设置选中回调
                .setOnCalendarSelectDayListener(new OnCalendarSelectDayListener<CalendarDay>() {
                    @Override
                    public void onCalendarSelectDay(CalendarSelectDay<CalendarDay> calendarSelectDay) {
                        CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
                        CalendarDay lastSelectDay = calendarSelectDay.getLastSelectDay();
                        if (firstSelectDay != null) {
                            String firstSelectDateStr = formatDate("yyyy-MM-dd", firstSelectDay.toDate());
                            tvFirstSelectDate.setText(firstSelectDateStr);
                        } else {
                            tvFirstSelectDate.setText("");
                        }
                        if (lastSelectDay != null) {
                            String lastSelectDateStr = formatDate("yyyy-MM-dd", lastSelectDay.toDate());
                            tvLastSelectDate.setText(lastSelectDateStr);
                        } else {
                            tvLastSelectDate.setText("");
                        }
                    }
                })
                //头部月份是否悬停
                .setStick(true)
                //是否展示头部月份
                .setShowMonthTitleView(true)
                //设置展示头部月份的回调用于创建头部月份View
                .setMonthTitleViewCallBack(new MonthTitleViewCallBack() {
                    @Override
                    public View getMonthTitleView(int position, Date date) {
                        View view = View.inflate(RangeSelectActivity.this, R.layout.layout_calendar_month_title, null);
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
    }


    /**
     * 默认选择当前日期到下一个月1号
     */
    private void initSelectCalendar() {
        calendarSelectDay = new CalendarSelectDay<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendarSelectDay.setFirstSelectDay(new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)));
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendarSelectDay.setLastSelectDay(new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)));
    }


    public String formatDate(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

}
