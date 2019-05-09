package com.sunyuan.calendarlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.model.CalendarSelectDay;


/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class CalendarView extends RecyclerView {


    public static final String TAG = CalendarView.class.getSimpleName();
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

    /**
     * 设置RecycleView itemDecoration,设置数据时会回调MonthTitleViewCallBack中getMonthTitleView方法
     *
     * @param monthTitleViewCallBack
     */
    public void setMonthTitleViewCallBack(MonthTitleViewCallBack monthTitleViewCallBack) {
        boolean isShowMonthTitleView = calendarAdapter.isShowMonthTitleView();
        if (isShowMonthTitleView) {
            MonthTitleDecoration monthTitleDecoration = new MonthTitleDecoration();
            monthTitleDecoration.setMonthTitleViewCallBack(monthTitleViewCallBack);
            addItemDecoration(monthTitleDecoration);
        }
    }

    /**
     * 释放 MonthTitleDecoration中资源
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        int itemDecorationCount = getItemDecorationCount();
        for (int itemDecorationIndex = 0; itemDecorationIndex < itemDecorationCount; itemDecorationIndex++) {
            ItemDecoration itemDecoration = getItemDecorationAt(itemDecorationIndex);
            if (itemDecoration instanceof MonthTitleDecoration) {
                ((MonthTitleDecoration) itemDecoration).destroy();
                break;
            }
        }
    }


    public void setOnCalendarSelectDayListener(OnCalendarSelectDayListener onCalendarSelectDayListener) {
        calendarAdapter.setOnCalendarSelectDayListener(onCalendarSelectDayListener);
    }


    public void setCalendarSelectDay(CalendarSelectDay calendarSelectDay) {
        calendarAdapter.setCalendarSelectDay(calendarSelectDay);
    }


    public void refresh() {
        calendarAdapter.refresh();
    }


    public int covertToPosition(@NonNull CalendarDay calendarDay) {
        int position = calendarAdapter.covertToPosition(calendarDay);
        Log.d(TAG, "covertToPosition:" + position);
        return position;
    }
}
