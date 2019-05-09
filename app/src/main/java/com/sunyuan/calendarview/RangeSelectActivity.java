package com.sunyuan.calendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sunyuan.calendarlibrary.CalendarView;
import com.sunyuan.calendarlibrary.MonthTitleViewCallBack;
import com.sunyuan.calendarlibrary.OnCalendarSelectDayListener;
import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.model.CalendarSelectDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RangeSelectActivity extends AppCompatActivity {
    public static final String TAG = RangeSelectActivity.class
            .getSimpleName();
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

        calendarView.setCalendarSelectDay(calendarSelectDay);
        //绘制monthTitle
        calendarView.setMonthTitleViewCallBack(new MonthTitleViewCallBack() {
            @Override
            public View getMonthTitleView(int position, Date date) {
                View view = View.inflate(RangeSelectActivity.this, R.layout.layout_month_title, null);
                TextView tvMonthTitle = view.findViewById(R.id.tv_month_title);
                tvMonthTitle.setText(formatDate("yyyy年MM月", date));
                return view;
            }
        });

        //设置选中事件
        calendarView.setOnCalendarSelectDayListener(new OnCalendarSelectDayListener<CalendarDay>() {
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
        calendarView.refresh();
        //根据指定日期得到position位置
        int position = calendarView.covertToPosition(calendarSelectDay.getFirstSelectDay());
        //滚动到制定位置
        calendarView.smoothScrollToPosition(position);
    }

    private void initSelectCalendar() {
        calendarSelectDay = new CalendarSelectDay<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year;
        int month;
        if (calendar.get(Calendar.MONTH) + 5 > 11) {
            year = calendar.get(Calendar.YEAR) + 1;
            month = 5;
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 5;
        }
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        CalendarDay firstSelectDay = new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (dayOfMonth <= maxDayOfMonth - 3) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 3);
        } else {
            int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
            month = calendar.get(Calendar.MONTH);
            if (month + 1 > maxMonth) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
            } else {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            }
            calendar.set(Calendar.DAY_OF_MONTH, 3 - (maxDayOfMonth - calendar.get(Calendar.DAY_OF_MONTH)));
        }
        CalendarDay lastSelelctDay = new CalendarDay(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendarSelectDay.setFirstSelectDay(firstSelectDay);
        calendarSelectDay.setLastSelectDay(lastSelelctDay);
    }


    public String formatDate(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

}
