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

public class SingleSelectActivity extends AppCompatActivity {

    private TextView tvCurrentSelectDate;
    private CalendarSelectDay<CalendarDay> calendarSelectDay;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_select);
        initSelectCalendar();
        initView();
    }

    private void initView() {
        calendarView = findViewById(R.id.calendar_view);
        tvCurrentSelectDate = findViewById(R.id.tv_current_select_date);
        String currentSelectDayStr = formatDate("yyyy-MM-dd", calendarSelectDay.getFirstSelectDay().toDate());
        tvCurrentSelectDate.setText(currentSelectDayStr);
        Calendar calendar = Calendar.getInstance();
        Date minDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, 25);
        Date maxDate = calendar.getTime();
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
                        View view = View.inflate(SingleSelectActivity.this, R.layout.layout_calendar_month_title, null);
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

    private void initSelectCalendar() {
        calendarSelectDay = new CalendarSelectDay<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 2);
        CalendarDay firstSelectDay = new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        calendarSelectDay.setFirstSelectDay(firstSelectDay);
    }


    public String formatDate(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }
}
