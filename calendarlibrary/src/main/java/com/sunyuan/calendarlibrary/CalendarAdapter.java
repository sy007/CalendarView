package com.sunyuan.calendarlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
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
import java.util.Objects;

import static com.sunyuan.calendarlibrary.MonthView.ATTRS;
import static com.sunyuan.calendarlibrary.MonthView.BOTTOM_TEXT_SIZE;
import static com.sunyuan.calendarlibrary.MonthView.CORNER_RADIUS;
import static com.sunyuan.calendarlibrary.MonthView.DEFAULT_DIS_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DEFAULT_SAME_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DEFAULT_SELECT_BG_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DEFAULT_SELECT_RANGE_BG_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DEFAULT_SELECT_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DEFAULT_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DEFAULT_TOP_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DEFAULT_WEEKEND_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DIS_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DIVIDER_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.DIVIDER_HEIGHT;
import static com.sunyuan.calendarlibrary.MonthView.FIRST_SELECT_DAY_TEXT;
import static com.sunyuan.calendarlibrary.MonthView.FIRST_TOP_MARGIN;
import static com.sunyuan.calendarlibrary.MonthView.LAST_SELECT_DAY_TEXT;
import static com.sunyuan.calendarlibrary.MonthView.MAX_DATE;
import static com.sunyuan.calendarlibrary.MonthView.MIN_DATE;
import static com.sunyuan.calendarlibrary.MonthView.MONTH_PADDING_BOTTOM;
import static com.sunyuan.calendarlibrary.MonthView.MONTH_PADDING_LEFT;
import static com.sunyuan.calendarlibrary.MonthView.MONTH_PADDING_RIGHT;
import static com.sunyuan.calendarlibrary.MonthView.MONTH_PADDING_TOP;
import static com.sunyuan.calendarlibrary.MonthView.ROW_HEIGHT;
import static com.sunyuan.calendarlibrary.MonthView.SAME_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.SECOND_TOP_MARGIN;
import static com.sunyuan.calendarlibrary.MonthView.SELECTION_MODE;
import static com.sunyuan.calendarlibrary.MonthView.SELECT_BG_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.SELECT_MAX_RANGE;
import static com.sunyuan.calendarlibrary.MonthView.SELECT_RANGE_BG_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.SELECT_STYLE;
import static com.sunyuan.calendarlibrary.MonthView.SELECT_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.TEXT_SIZE;
import static com.sunyuan.calendarlibrary.MonthView.THIRD_TOP_MARGIN;
import static com.sunyuan.calendarlibrary.MonthView.TOP_SIZE;
import static com.sunyuan.calendarlibrary.MonthView.TOP_TEXT_COLOR;
import static com.sunyuan.calendarlibrary.MonthView.WEEKEND_TEXT_COLOR;


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
    private OnCalendarSelectDayListener<CalendarDay> onCalendarSelectDayListener;
    private SparseArray<Date> monthTitleMap;
    private int marginLeft;
    private int marginTop;
    private int marginRight;
    private int marginBottom;
    private int itemCount;
    private SelectionMode selectionMode;
    private Calendar minCalendar;
    private Calendar maxCalendar;


    public CalendarAdapter(Context context, TypedArray typedArray) {
        calendar = Calendar.getInstance();
        parseTypedArray(context, typedArray);
    }


    private void parseTypedArray(Context context, TypedArray typedArray) {
        int rowHeight = (int) typedArray.getDimension(R.styleable.CalendarView_rowHeight, Utils.dip2px(context, 64));
        int textColor = typedArray.getColor(R.styleable.CalendarView_textColor, DEFAULT_TEXT_COLOR);
        int selectTextColor = typedArray.getColor(R.styleable.CalendarView_selectTextColor, DEFAULT_SELECT_TEXT_COLOR);
        int selectBgColor = typedArray.getColor(R.styleable.CalendarView_selectBgColor, DEFAULT_SELECT_BG_COLOR);
        int weekendTextColor = typedArray.getColor(R.styleable.CalendarView_weekendTextColor, DEFAULT_WEEKEND_TEXT_COLOR);
        int disTextColor = typedArray.getColor(R.styleable.CalendarView_disTextColor, DEFAULT_DIS_TEXT_COLOR);
        int topTextColor = typedArray.getColor(R.styleable.CalendarView_topTextColor, DEFAULT_TOP_TEXT_COLOR);
        int sameTextColor = typedArray.getColor(R.styleable.CalendarView_sameTextColor, DEFAULT_SAME_TEXT_COLOR);
        int selectRangeBgColor = typedArray.getColor(R.styleable.CalendarView_selectRangebgColor, DEFAULT_SELECT_RANGE_BG_COLOR);

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


    public void refresh() {
        notifyDataSetChanged();
    }


    public int covertToPosition(CalendarDay calendarDay) {
        int itemCount = getItemCount();
        if (itemCount == 0) {
            return -1;
        }
        int calYear = calendarDay.toCalendar().get(Calendar.YEAR);
        int calMonth = calendarDay.toCalendar().get(Calendar.MONTH);
        int minYear = minCalendar.get(Calendar.YEAR);
        int minMonth = minCalendar.get(Calendar.MONTH);
        int maxYear = maxCalendar.get(Calendar.YEAR);
        int maxMonth = maxCalendar.get(Calendar.MONTH);
        int calToMaxCalDiffMonth = (maxYear * MONTH_IN_YEAR + maxMonth) - (calYear * MONTH_IN_YEAR + calMonth);
        int calToMinCalDiffMonth = (calYear * MONTH_IN_YEAR + calMonth) - (minYear * MONTH_IN_YEAR + minMonth);
        if (calToMaxCalDiffMonth < 0 || calToMinCalDiffMonth < 0) {
            return -1;
        }
        int position = (calToMinCalDiffMonth % MONTH_IN_YEAR) + ((calToMaxCalDiffMonth / MONTH_IN_YEAR) * MONTH_IN_YEAR);
        CalendarLog.d("position:" + position);
        return position;
    }


    @Override
    public int getItemCount() {
        return itemCount;
    }

    @Override
    public Date getMonthDate(int position) {
        return monthTitleMap.get(position);
    }


    @Override
    public void onDayClick(CalendarDay calendarDay) {
        if (onCalendarSelectDayListener != null) {
            if (SelectionMode.SINGLE == selectionMode) {
                calendarSelectDay.setFirstSelectDay(calendarDay);
            } else {
                CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
                if (firstSelectDay != null) {
                    CalendarDay lastSelectDay = calendarSelectDay.getLastSelectDay();
                    if (lastSelectDay != null) {
                        calendarSelectDay.setFirstSelectDay(calendarDay);
                        calendarSelectDay.setLastSelectDay(null);
                    } else {
                        int i = calendarDay.toDate().compareTo(firstSelectDay.toDate());
                        switch (i) {
                            case 0:
                                calendarSelectDay.setFirstSelectDay(calendarDay);
                                break;
                            case 1:
                                calendarSelectDay.setLastSelectDay(calendarDay);
                                break;
                            case -1:
                                calendarSelectDay.setFirstSelectDay(calendarDay);
                                break;
                        }
                    }
                } else {
                    calendarSelectDay.setFirstSelectDay(calendarDay);
                }
            }
            onCalendarSelectDayListener.onCalendarSelectDay(calendarSelectDay);
        }
        notifyDataSetChanged();
    }


    void init(CalendarViewWrapper.CalendarBuilder calendarBuilder) {
        if (calendarBuilder.isShowMonthTitleView) {
            monthTitleMap = new SparseArray<>();
        }
        Date minDate = Objects.requireNonNull(calendarBuilder.minDate);
        Date maxDate = Objects.requireNonNull(calendarBuilder.maxDate);
        minCalendar = Calendar.getInstance();
        minCalendar.setTime(minDate);
        maxCalendar = Calendar.getInstance();
        maxCalendar.setTime(maxDate);
        itemCount = Utils.getMonthDiff(minCalendar, maxCalendar) + 1;
        calendar.setTime(minDate);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        selectionMode = calendarBuilder.selectionMode;
        onCalendarSelectDayListener = calendarBuilder.onCalendarSelectDayListener;
        calendarSelectDay = calendarBuilder.calendarSelectDay;
        ATTRS.put(SELECTION_MODE, selectionMode);
        ATTRS.put(MIN_DATE, minCalendar);
        ATTRS.put(MAX_DATE, maxCalendar);
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
