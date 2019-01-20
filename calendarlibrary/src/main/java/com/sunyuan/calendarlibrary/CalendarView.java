package com.sunyuan.calendarlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sunyuan.calendarlibrary.model.CalendarSelectDay;
import com.sunyuan.calendarlibrary.utils.Utils;

import static com.sunyuan.calendarlibrary.MonthLableView.*;


/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class CalendarView extends LinearLayout {


    private CalendarAdapter calendarAdapter;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        parseMonthLableAttrs(context, typedArray);
        LayoutParams monthLableLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        MonthLableView monthLableView = new MonthLableView(context);
        monthLableView.setLayoutParams(monthLableLayoutParams);
        addView(monthLableView);

        RecyclerView recMonthView = new RecyclerView(context);
        LayoutParams recMonthLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        recMonthView.setLayoutParams(recMonthLayoutParams);
        recMonthView.setLayoutManager(new LinearLayoutManager(context));
        recMonthView.addItemDecoration(new MonthTitleDecoration(context, typedArray));
        calendarAdapter = new CalendarAdapter(context, typedArray);
        recMonthView.setAdapter(calendarAdapter);
        addView(recMonthView);
        typedArray.recycle();
    }


    private void parseMonthLableAttrs(Context context, TypedArray typedArray) {
        ATTRS.put(LABLE_WEEKEND_TEXT_COLOR, typedArray.getColor(R.styleable.CalendarView_lableWeekendTextColor, Color.parseColor("#FF6E00")));
        ATTRS.put(LABLE_TEXT_COLOR, typedArray.getColor(R.styleable.CalendarView_lableTextColor, Color.parseColor("#000000")));
        ATTRS.put(LABLE_TEXT_SIZE, (int) (typedArray.getDimension(R.styleable.CalendarView_lableTextSize, Utils.sp2px(context, 13))));
        ATTRS.put(LABLE_PADDING_LEFT, (int) (typedArray.getDimension(R.styleable.CalendarView_lablePaddingLeft, 0)));
        ATTRS.put(LABLE_PADDING_TOP, (int) (typedArray.getDimension(R.styleable.CalendarView_lablePaddingTop, 0)));
        ATTRS.put(LABLE_PADDING_RIGHT, (int) (typedArray.getDimension(R.styleable.CalendarView_lablePaddingRight, 0)));
        ATTRS.put(LABLE_PADDING_BOTTOM, (int) (typedArray.getDimension(R.styleable.CalendarView_lablePaddingBottom, 0)));
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
