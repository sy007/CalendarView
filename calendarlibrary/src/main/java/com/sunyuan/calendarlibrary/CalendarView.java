package com.sunyuan.calendarlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.sunyuan.calendarlibrary.model.CalendarSelectDay;
import com.sunyuan.calendarlibrary.utils.Utils;

import static com.sunyuan.calendarlibrary.MonthLableView.*;


/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class CalendarView extends RecyclerView {


    private CalendarAdapter calendarAdapter;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        setLayoutManager(new LinearLayoutManager(context));
        calendarAdapter = new CalendarAdapter(context, typedArray);
        setAdapter(calendarAdapter);
        typedArray.recycle();
    }


    public void setOnCalendarSelectDayListener(OnCalendarSelectDayListener onCalendarSelectDayListener) {
        calendarAdapter.setOnCalendarSelectDayListener(onCalendarSelectDayListener);
    }

    public void setCalendarSelectDay(CalendarSelectDay calendarSelectDay) {
        calendarAdapter.setCalendarSelectDay(calendarSelectDay);
        calendarAdapter.notifyDataSetChanged();
    }


    public interface OnCalendarSelectDayListener<K> {
        void onCalendarSelectDay(CalendarSelectDay<K> calendarSelectDay);
    }
}
