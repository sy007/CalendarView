package com.sunyuan.calendarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        /**
         * 设置初始化选中日期
         * 当只可单选时,只需要设置CalendarSelectDay 中firstSelectDay
         */
        calendarView.setCalendarSelectDay(calendarSelectDay);
        //绘制monthTitle
        calendarView.setMonthTitleViewCallBack(new MonthTitleViewCallBack() {
            @Override
            public View getMonthTitleView(int position, Date date) {
                View view = View.inflate(SingleSelectActivity.this, R.layout.layout_month_title, null);
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
                /**
                 * 当isSingleSelect为true时。firstSelectDay是选择后的日期 lastSelectDay 为null
                 * 当isSingleSelect为false时,firstSelectDay为第一次选择的日期,lastSelectDay为最后一次选择的日期
                 */
                if (firstSelectDay != null) {
                    String firstSelectDateStr = formatDate("yyyy-MM-dd", firstSelectDay.toDate());
                    tvCurrentSelectDate.setText(firstSelectDateStr);
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
        if (calendar.get(Calendar.MONTH) + 1 > 11) {
            year = calendar.get(Calendar.YEAR) + 1;
            month = 1;
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
        }
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        CalendarDay firstSelectDay = new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendarSelectDay.setFirstSelectDay(firstSelectDay);
    }


    public String formatDate(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }
}
