package com.sunyuan.calendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.model.CalendarSelectDay;
import com.sunyuan.calendarlibrary.CalendarView;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    public static final String TAG =MainActivity.class
            .getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendarView = findViewById(R.id.calendar_view);

        CalendarSelectDay<CalendarDay> calendarSelectDay = new CalendarSelectDay<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        CalendarDay firstSelectDay = new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (dayOfMonth <= maxDayOfMonth - 3) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 3);
        } else {
            int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
            int month = calendar.get(Calendar.MONTH);
            if (month + 1 > maxMonth) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
            } else {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            }
            calendar.set(Calendar.DAY_OF_MONTH, 3 - (maxDayOfMonth - calendar.get(Calendar.DAY_OF_MONTH)));
        }
        CalendarDay lastSelelctDay = new CalendarDay(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Log.d(TAG,"firstSelectDay:"+firstSelectDay.toString());
        Log.d(TAG,"lastSelelctDay:"+lastSelelctDay.toString());
        calendarSelectDay.setFirstSelectDay(firstSelectDay);
        calendarSelectDay.setLastSelectDay(lastSelelctDay);
        calendarView.setCalendarSelectDay(calendarSelectDay);
        calendarView.setOnCalendarSelectDayListener(new CalendarView.OnCalendarSelectDayListener<CalendarDay>() {
            @Override
            public void onCalendarSelectDay(CalendarSelectDay<CalendarDay> calendarSelectDay) {
                StringBuffer stringBuffer = new StringBuffer();
                CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
                CalendarDay lastSelectDay = calendarSelectDay.getLastSelectDay();
                if (firstSelectDay != null) {
                    stringBuffer.append(firstSelectDay.toString());
                }
                if (lastSelectDay != null) {
                    stringBuffer.append(lastSelectDay.toString());
                }
                Toast.makeText(MainActivity.this, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
