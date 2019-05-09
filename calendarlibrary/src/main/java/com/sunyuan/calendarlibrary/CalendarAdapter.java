package com.sunyuan.calendarlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.model.CalendarSelectDay;
import com.sunyuan.calendarlibrary.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sunyuan.calendarlibrary.MonthView.*;


/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
final class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder> implements
        MonthView.OnDayClickListener, MonthTitleDecoration.MonthDateCallback {
    private static final int MONTH_IN_YEAR = 12;
    private final Calendar calendar;
    private int currentMonth;
    private int currentYear;
    private CalendarSelectDay<CalendarDay> calendarSelectDay;
    private Map<Integer, Date> monthTitleMap;
    private Map<CalendarDay, Integer> calendarDayToPositionMap = new HashMap<>();
    private boolean isShowMonthTitleView;
    private int marginLeft;
    private int marginTop;
    private int marginRight;
    private int marginBottom;
    private boolean isSingleSelect;

    public CalendarAdapter(Context context, TypedArray typedArray) {
        calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        parseTypedArray(context, typedArray);
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (isShowMonthTitleView) {
            monthTitleMap = new HashMap<>();
        }
    }

    private void parseTypedArray(Context context, TypedArray typedArray) {
        isShowMonthTitleView = typedArray.getBoolean(R.styleable.CalendarView_isShowMonthTitleView, true);
        isSingleSelect = typedArray.getBoolean(R.styleable.CalendarView_isSingleSelect, false);
        int textColor = typedArray.getColor(R.styleable.CalendarView_textColor, DEFAULT_TEXT_COLOR);
        int selectTextColor = typedArray.getColor(R.styleable.CalendarView_selectTextColor, DEFAULT_SELECT_TEXT_COLOR);
        int selectBgColor = typedArray.getColor(R.styleable.CalendarView_selectBgColor, DEFAULT_SELECT_BG_COLOR);
        int weekendTextColor = typedArray.getColor(R.styleable.CalendarView_weekendTextColor, DEFAULT_WEEKEND_TEXT_COLOR);
        int disTextColor = typedArray.getColor(R.styleable.CalendarView_disTextColor, DEFAULT_DIS_TEXT_COLOR);
        int topTextColor = typedArray.getColor(R.styleable.CalendarView_topTextColor, DEFAULT_TOP_TEXT_COLOR);
        int sameTextColor = typedArray.getColor(R.styleable.CalendarView_sameTextColor, DEFAULT_SAME_TEXT_COLOR);
        int selectRangeBgColor = typedArray.getColor(R.styleable.CalendarView_selectRangebgColor, DEFAULT_SELECT_RANGE_BG_COLOR);

        int rowHeight = Utils.dip2px(context, 75);
        int defaultTopSize = Utils.sp2px(context, 10);
        int defaultTextSize = Utils.sp2px(context, 13);
        int defaultBottomTextSize = Utils.sp2px(context, 10);
        int defaultFirstTopMargin = Utils.dip2px(context, 5);
        int defaultSecondTopMargin = Utils.dip2px(context, 5);
        int defaultThirdTopMargin = Utils.dip2px(context, 5);
        int defaultCornerRadius = Utils.dip2px(context, 3);
        int topTextSize = (int) typedArray.getDimension(R.styleable.CalendarView_topTextSize, defaultTopSize);
        int textSize = (int) typedArray.getDimension(R.styleable.CalendarView_textSize, defaultTextSize);
        int bottomTextSize = (int) typedArray.getDimension(R.styleable.CalendarView_bottomTextSize, defaultBottomTextSize);
        int firstTopMargin = (int) typedArray.getDimension(R.styleable.CalendarView_firstTopMargin, defaultFirstTopMargin);
        int secondTopMargin = (int) typedArray.getDimension(R.styleable.CalendarView_secondTopMargin, defaultSecondTopMargin);
        int thirdTopMargin = (int) typedArray.getDimension(R.styleable.CalendarView_thirdTopMargin, defaultThirdTopMargin);
        int selectMaxRange = typedArray.getInteger(R.styleable.CalendarView_selectMaxRange, MonthView.DEFAULT_SELECT_MAX_RANGE);
        int dividerHeight = (int) typedArray.getDimension(R.styleable.CalendarView_dividerHeight, MonthView.DEFAULT_DIVIDER_HEIGHT);
        int dividerColor = typedArray.getColor(R.styleable.CalendarView_dividerColor, MonthView.DEFAULT_DIVIDER_COLOR);
        int selectStyle = typedArray.getInt(R.styleable.CalendarView_selectStyle, 0);
        int cornerRadius = (int) typedArray.getDimension(R.styleable.CalendarView_cornerRadius, defaultCornerRadius);
        String firstSelectDayText = typedArray.getString(R.styleable.CalendarView_firstSelectDayText);
        String lastSelectDayText = typedArray.getString(R.styleable.CalendarView_lastSelectDayText);

        marginLeft = (int) typedArray.getDimension(R.styleable.CalendarView_monthMarginLeft, 0);
        marginTop = (int) typedArray.getDimension(R.styleable.CalendarView_monthMarginTop, 0);
        marginRight = (int) typedArray.getDimension(R.styleable.CalendarView_monthMarginRight, 0);
        marginBottom = (int) typedArray.getDimension(R.styleable.CalendarView_monthMarginBottom, 0);

        int paddingLeft = (int) typedArray.getDimension(R.styleable.CalendarView_monthPaddingLeft, 0);
        int paddingTop = (int) typedArray.getDimension(R.styleable.CalendarView_monthPaddingTop, 0);
        int paddingRight = (int) typedArray.getDimension(R.styleable.CalendarView_monthPaddingRight, 0);
        int paddingBottom = (int) typedArray.getDimension(R.styleable.CalendarView_monthPaddingBottom, 0);
        ATTRS.put(SELECT_STYLE, selectStyle);
        ATTRS.put(CORNER_RADIUS, cornerRadius);
        ATTRS.put(MONTH_PADDING_LEFT, paddingLeft);
        ATTRS.put(MONTH_PADDING_TOP, paddingTop);
        ATTRS.put(MONTH_PADDING_RIGHT, paddingRight);
        ATTRS.put(MONTH_PADDING_BOTTOM, paddingBottom);
        ATTRS.put(TOP_TEXT_COLOR, topTextColor);
        ATTRS.put(TEXT_COLOR, textColor);
        ATTRS.put(SELECT_TEXT_COLOR, selectTextColor);
        ATTRS.put(SELECT_BG_COLOR, selectBgColor);
        ATTRS.put(SELECT_RANGE_BG_COLOR, selectRangeBgColor);
        ATTRS.put(WEEKEND_TEXT_COLOR, weekendTextColor);
        ATTRS.put(DIS_TEXT_COLOR, disTextColor);
        ATTRS.put(SAME_TEXT_COLOR, sameTextColor);
        ATTRS.put(SELECT_MAX_RANGE, selectMaxRange);
        ATTRS.put(DIVIDER_HEIGHT, dividerHeight);
        ATTRS.put(DIVIDER_COLOR, dividerColor);
        ATTRS.put(TOP_SIZE, topTextSize);
        ATTRS.put(TEXT_SIZE, textSize);
        ATTRS.put(BOTTOM_TEXT_SIZE, bottomTextSize);
        ATTRS.put(FIRST_TOP_MARGIN, firstTopMargin);
        ATTRS.put(SECOND_TOP_MARGIN, secondTopMargin);
        ATTRS.put(THIRD_TOP_MARGIN, thirdTopMargin);
        ATTRS.put(ROW_HEIGHT, rowHeight);
        ATTRS.put(FIRST_SELECT_DAY_TEXT, firstSelectDayText);
        ATTRS.put(LAST_SELECT_DAY_TEXT, lastSelectDayText);
        ATTRS.put(IS_SINGLE_SELECT, isSingleSelect);
    }

    @NonNull
    @Override
    public CalendarHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_month_view, viewGroup,
                false);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        view.setLayoutParams(layoutParams);
        return new CalendarAdapter.CalendarHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarHolder calendarHolder, int position) {
        int month = (currentMonth + (position % MONTH_IN_YEAR)) % MONTH_IN_YEAR;
        int year = (currentMonth + position) / MONTH_IN_YEAR + currentYear;
        if (monthTitleMap != null) {
            monthTitleMap.put(position, getMonthDate(year, month));
        }
        calendarDayToPositionMap.put(new CalendarDay(year, month, -1), position);
        Map<String, Integer> parmas = new HashMap<>();
        if (calendarSelectDay == null) {
            calendarSelectDay = new CalendarSelectDay<>();
        }
        CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
        CalendarDay lastSelectDay = calendarSelectDay.getLastSelectDay();
        int firstYear = -1;
        int firstMonth = -1;
        int firstDay = -1;
        int lastYear = -1;
        int lastMonth = -1;
        int lastDay = -1;
        if (firstSelectDay != null) {
            firstYear = firstSelectDay.getYear();
            firstMonth = firstSelectDay.getMonth();
            firstDay = firstSelectDay.getDay();
        }

        if (lastSelectDay != null) {
            lastYear = lastSelectDay.getYear();
            lastMonth = lastSelectDay.getMonth();
            lastDay = lastSelectDay.getDay();
        }
        parmas.put(MonthView.VIEW_FIRST_SELECT_YEAR, firstYear);
        parmas.put(MonthView.VIEW_FIRST_SELECT_MONTH, firstMonth);
        parmas.put(MonthView.VIEW_FIRST_SELECT_DAY, firstDay);
        parmas.put(MonthView.VIEW_YEAR, year);
        parmas.put(MonthView.VIEW_MONTH, month);
        parmas.put(MonthView.VIEW_LAST_SELECT_YEAR, lastYear);
        parmas.put(MonthView.VIEW_LAST_SELECT_MONTH, lastMonth);
        parmas.put(MonthView.VIEW_LAST_SELECT_DAY, lastDay);
        calendarHolder.monthView.setParams(parmas);
    }

    private Date getMonthDate(int year, int month) {
        Calendar calendar = this.calendar;
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }


    @Override
    public void onDayClick(CalendarDay calendarDay) {
        CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
        if (isSingleSelect) {
            calendarSelectDay.setFirstSelectDay(calendarDay);
        } else {
            if (firstSelectDay != null) {
                CalendarDay lastSelectDay = calendarSelectDay.getLastSelectDay();
                if (lastSelectDay != null) {
                    calendarSelectDay.setFirstSelectDay(calendarDay);
                    calendarSelectDay.setLastSelectDay(null);
                } else {
                    if (calendarDay.toDate().before(firstSelectDay.toDate())) {
                        calendarSelectDay.setFirstSelectDay(calendarDay);
                    } else {
                        calendarSelectDay.setLastSelectDay(calendarDay);
                    }
                }
            } else {
                calendarSelectDay.setFirstSelectDay(calendarDay);
            }
        }
        if (calendarSelectDayListener != null) {
            calendarSelectDayListener.onCalendarSelectDay(calendarSelectDay);
        }
        notifyDataSetChanged();
    }


    private OnCalendarSelectDayListener calendarSelectDayListener;


    public void setOnCalendarSelectDayListener(OnCalendarSelectDayListener onCalendarSelectDayListener) {
        this.calendarSelectDayListener = onCalendarSelectDayListener;
    }


    public void setCalendarSelectDay(CalendarSelectDay calendarSelectDay) {
        this.calendarSelectDay = calendarSelectDay;
    }


    public void refresh() {
        notifyDataSetChanged();
    }


    public int covertToPosition(CalendarDay calendarDay) {
        int year = calendarDay.getYear();
        int month = calendarDay.getMonth();
        int position = (year * MONTH_IN_YEAR + month) - (currentYear * MONTH_IN_YEAR + currentMonth);
        return position > MONTH_IN_YEAR ? 0 : position;
    }


    @Override
    public int getItemCount() {
        return MONTH_IN_YEAR;
    }

    @Override
    public Date getMonthDate(int position) {
        return monthTitleMap.get(position);
    }

    public boolean isShowMonthTitleView() {
        return isShowMonthTitleView;
    }

    public static class CalendarHolder extends RecyclerView.ViewHolder {

        private MonthView monthView;

        public CalendarHolder(@NonNull View itemView, MonthView.OnDayClickListener onDayClickListener) {
            super(itemView);
            monthView = itemView.findViewById(R.id.item_month_view);
            monthView.setOnDayClickListener(onDayClickListener);
        }
    }
}
