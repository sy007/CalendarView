package com.sunyuan.calendarlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.utils.Lunar;
import com.sunyuan.calendarlibrary.utils.LunarSolarConverter;
import com.sunyuan.calendarlibrary.utils.Solar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class MonthView extends View {
    public int rowNum = 6;
    public int rowHeight;
    public int columnNum = 7;

    private Calendar calendar;

    private int year;
    private int month;
    private int dayWidth;
    private Paint dayPaint;
    private Paint thirdPaint;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private Rect dayRang;

    /**
     * 日历参数
     */
    public static final String VIEW_YEAR = "VIEW_YEAR";
    public static final String VIEW_MONTH = "VIEW_MONTH";
    public static final String VIEW_FIRST_SELECT_YEAR = "VIEW_FIRST_SELECT_YEAR";
    public static final String VIEW_FIRST_SELECT_MONTH = "VIEW_FIRST_SELECT_MONTH";
    public static final String VIEW_FIRST_SELECT_DAY = "VIEW_FIRST_SELECT_DAY";
    public static final String VIEW_LAST_SELECT_YEAR = "VIEW_LAST_SELECT_YEAR";
    public static final String VIEW_LAST_SELECT_MONTH = "VIEW_LAST_SELECT_MONTH";
    public static final String VIEW_LAST_SELECT_DAY = "VIEW_LAST_SELECT_DAY";

    /**
     * 自定义属性参数
     */
    public static final Map<String, Object> ATTRS = new HashMap<>();
    public static final String TOP_TEXT_COLOR = "TOP_TEXT_COLOR";
    public static final String TEXT_COLOR = "TEXT_COLOR";
    public static final String SELECT_TEXT_COLOR = "SELECT_TEXT_COLOR";
    public static final String SELECT_BG_COLOR = "SELECT_BG_COLOR";
    public static final String SELECT_RANGE_BG_COLOR = "SELECT_RANGE_BG_COLOR";
    public static final String WEEKEND_TEXT_COLOR = "WEEKEND_TEXT_COLOR";
    public static final String DIS_TEXT_COLOR = "DIS_TEXT_COLOR";
    public static final String SAME_TEXT_COLOR = "SAME_TEXT_COLOR";
    public static final String SELECT_MAX_RANGE = "SELECT_MAX_RANGE";
    public static final String DIVIDER_HEIGHT = "DIVIDER_HEIGHT";
    public static final String DIVIDER_COLOR = "DIVIDER_COLOR";
    public static final String TOP_SIZE = "TOP_SIZE";
    public static final String TEXT_SIZE = "TEXT_SIZE";
    public static final String BOTTOM_TEXT_SIZE = "BOTTOM_TEXT_SIZE";
    public static final String FIRST_TOP_MARGIN = "FIRST_TOP_MARGIN";
    public static final String SECOND_TOP_MARGIN = "SECOND_TOP_MARGIN";
    public static final String THIRD_TOP_MARGIN = "THIRD_TOP_MARGIN";
    public static final String ROW_HEIGHT = "ROW_HEIGHT";
    public static final String MONTH_PADDING_LEFT = "MONTH_PADDING_LEFT";
    public static final String MONTH_PADDING_TOP = "MONTH_PADDING_TOP";
    public static final String MONTH_PADDING_RIGHT = "MONTH_PADDING_RIGHT";
    public static final String MONTH_PADDING_BOTTOM = "MONTH_PADDING_BOTTOM";
    public static final String SELECT_STYLE = "SELECT_STYLE";
    public static final String CORNER_RADIUS = "CORNER_RADIUS";
    public static final String FIRST_SELECT_DAY_TEXT = "FIRST_SELECT_DAY_TEXT";
    public static final String LAST_SELECT_DAY_TEXT = "LAST_SELECT_DAY_TEXT";
    public static final String IS_SINGLE_SELECT = "IS_SINGLE_SELECT";

    public static final int DEFAULT_TOP_TEXT_COLOR = Color.parseColor("#FF6E00");
    public static final int DEFAULT_TEXT_COLOR = Color.parseColor("#000000");
    public static final int DEFAULT_SELECT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    public static final int DEFAULT_SELECT_BG_COLOR = Color.parseColor("#3A5FCD");
    public static final int DEFAULT_SELECT_RANGE_BG_COLOR = Color.parseColor("#4C3A5FCD");
    public static final int DEFAULT_WEEKEND_TEXT_COLOR = Color.parseColor("#FF6E00");
    public static final int DEFAULT_DIS_TEXT_COLOR = Color.parseColor("#BBBBBB");
    public static final int DEFAULT_SAME_TEXT_COLOR = Color.parseColor("#FF6E00");
    public static final int DEFAULT_SELECT_MAX_RANGE = -1;
    public static final int DEFAULT_DIVIDER_HEIGHT = 0;
    public static final int DEFAULT_DIVIDER_COLOR = 0;
    public static final String TODAY_TEXT = "今天";

    private int firstYear;
    private int firstMonth;
    private int firstDay;
    private int lastYear;
    private int lastMonth;
    private int lastDay;
    private int textColor;
    private int selectTextColor;
    private int selectBgColor;
    private int weekendTextColor;
    private int disTextColor;
    private int bottomTextSize;
    private int textSize;
    private int topTextSize;
    private int firstTopMargin;
    private int secondTopMargin;
    private int sameTextColor;
    private int selectMaxRange;
    private Paint.FontMetrics fm;


    private int firstDayOfYear;
    private int dayOfYear;
    private int lastDayOfYear;
    private Paint selectPaint;
    private Rect selectRangeRect;
    private int selectRangeBgColor;
    private Map<Integer, String> festivalMap = new HashMap<>();
    private Solar solar;
    private Paint firstSelectPaint;
    private int topTextColor;
    private int thirdTopMargin;
    private int dividerHeight;
    private int dividerColor;
    private Paint dividerPaint;
    private int selectStyle;
    private int cornerRadius;
    private String firstSelectDayText;
    private String lastSelectDayText;
    private boolean isSingleSelect;


    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textColor = (int) ATTRS.get(TEXT_COLOR);
        selectTextColor = (int) ATTRS.get(SELECT_TEXT_COLOR);
        selectBgColor = (int) ATTRS.get(SELECT_BG_COLOR);
        weekendTextColor = (int) ATTRS.get(WEEKEND_TEXT_COLOR);
        disTextColor = (int) ATTRS.get(DIS_TEXT_COLOR);
        topTextColor = (int) ATTRS.get(TOP_TEXT_COLOR);
        sameTextColor = (int) ATTRS.get(SAME_TEXT_COLOR);
        selectRangeBgColor = (int) ATTRS.get(SELECT_RANGE_BG_COLOR);
        topTextSize = (int) ATTRS.get(TOP_SIZE);
        textSize = (int) ATTRS.get(TEXT_SIZE);
        bottomTextSize = (int) ATTRS.get(BOTTOM_TEXT_SIZE);
        firstTopMargin = (int) ATTRS.get(FIRST_TOP_MARGIN);
        secondTopMargin = (int) ATTRS.get(SECOND_TOP_MARGIN);
        thirdTopMargin = (int) ATTRS.get(THIRD_TOP_MARGIN);
        selectMaxRange = (int) ATTRS.get(SELECT_MAX_RANGE);
        dividerHeight = (int) ATTRS.get(DIVIDER_HEIGHT);
        dividerColor = (int) ATTRS.get(DIVIDER_COLOR);
        rowHeight = (int) ATTRS.get(ROW_HEIGHT);
        selectStyle = (int) ATTRS.get(SELECT_STYLE);
        cornerRadius = (int) ATTRS.get(CORNER_RADIUS);
        firstSelectDayText = (String) ATTRS.get(FIRST_SELECT_DAY_TEXT);
        lastSelectDayText = (String) ATTRS.get(LAST_SELECT_DAY_TEXT);
        isSingleSelect = (boolean) ATTRS.get(IS_SINGLE_SELECT);
        int paddingLeft = (int) ATTRS.get(MONTH_PADDING_LEFT);
        int paddingTop = (int) ATTRS.get(MONTH_PADDING_TOP);
        int paddingRight = (int) ATTRS.get(MONTH_PADDING_RIGHT);
        int paddingBottom = (int) ATTRS.get(MONTH_PADDING_BOTTOM);
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DATE);

        solar = new Solar();
        dayRang = new Rect();
        selectRangeRect = new Rect();
        fm = new Paint.FontMetrics();
        initPaint();
    }

    private void initPaint() {
        dayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dayPaint.setColor(textColor);
        dayPaint.setTextAlign(Paint.Align.CENTER);
        dayPaint.setTextSize(textSize);


        dividerPaint = new Paint();
        dividerPaint.setColor(dividerColor);

        selectPaint = new Paint();

        firstSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstSelectPaint.setTextSize(topTextSize);
        firstSelectPaint.setColor(topTextColor);
        firstSelectPaint.setTextAlign(Paint.Align.CENTER);

        thirdPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thirdPaint.setTextSize(bottomTextSize);
        thirdPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, (rowNum - 1) * dividerHeight + (rowNum * rowHeight) +
                getPaddingTop() + getPaddingBottom());

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dayWidth = (w - (getPaddingLeft() + getPaddingRight())) / columnNum;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDays(canvas);
        if (!isSingleSelect && lastDay != -1 && selectStyle >= 0) {
            drawRange(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                CalendarDay calendarDay = getDayFromLocation(event.getX(), event.getY());
                if (calendarDay != null) {
                    onDayClick(calendarDay);
                }
                break;
        }
        return true;
    }

    private OnDayClickListener onDayClickListener;

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }

    private void onDayClick(CalendarDay calendarDay) {
        if (onDayClickListener != null) {
            onDayClickListener.onDayClick(calendarDay);
        }
    }


    private CalendarDay getDayFromLocation(float x, float y) {
        if ((x < getPaddingLeft()) || (x > getWidth() - getPaddingRight())) {
            return null;
        }
        if ((y < getPaddingTop()) || (y > getHeight() - getPaddingBottom())) {
            return null;
        }
        int yDay = (int) ((y - getPaddingTop()) / (rowHeight + dividerHeight));

        int day = 1 + ((int) ((x - getPaddingLeft()) * columnNum / (getWidth() - getPaddingLeft() - getPaddingRight())) - findDayOffset(year, month, 1)) +
                yDay * columnNum;

        if (dayOfMonth(year, month) < day || day < 1 || isPreDay(day) || isMaxRange(day)) {
            return null;
        }

        return new CalendarDay(year, month, day);
    }


    @SuppressLint("DefaultLocale")
    private void drawDays(Canvas canvas) {
        String dayText;
        int dayOfMonth = dayOfMonth(year, month);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int top = paddingTop;
        for (int day = 1; day <= dayOfMonth; day++) {
            String festival = festivalMap.get(day);
            int dayOffset = findDayOffset(year, month, day);
            int offset = dayOffset * dayWidth + paddingLeft;
            dayRang.set(offset, top, offset + dayWidth, top + rowHeight);
            if (isFirstDay(day) || isLastDay(day)) {
                if (selectStyle >= 0) {
                    selectPaint.setColor(selectBgColor);
                    drawSelectRange(canvas, dayRang, selectPaint);
                }
            }
            dayText = String.format("%d", day);
            if (isSameDay(day)) {
                dayText = TODAY_TEXT;
            }
            if (isFirstDay(day) || isLastDay(day)) {
                dayPaint.setColor(selectTextColor);
                firstSelectPaint.setColor(selectTextColor);
                if (isFirstDay(day) && !TextUtils.isEmpty(firstSelectDayText)) {
                    drawSelectDayText(canvas, firstSelectDayText);
                }
                if (isLastDay(day) && !TextUtils.isEmpty(lastSelectDayText)) {
                    drawSelectDayText(canvas, lastSelectDayText);
                }
            } else {
                if (isPreDay(day)) {
                    firstSelectPaint.setColor(disTextColor);
                    dayPaint.setColor(disTextColor);
                } else {
                    firstSelectPaint.setColor(topTextColor);
                    if (isSameDay(day)) {
                        dayPaint.setColor(sameTextColor);
                    } else {
                        if (0 == dayOffset || columnNum - dayOffset == 1) {
                            dayPaint.setColor(weekendTextColor);
                        } else {
                            dayPaint.setColor(textColor);
                        }
                    }
                }
            }
            if (!isSingleSelect && isMaxRange(day)) {
                firstSelectPaint.setColor(disTextColor);
                dayPaint.setColor(disTextColor);
            }
            firstSelectPaint.getFontMetrics(fm);
            float dayFirstAscent = fm.ascent;
            float dayFirstDescent = fm.descent;
            float dayFirstHeight = dayFirstDescent - dayFirstAscent;
            if (!TextUtils.isEmpty(festival)) {
                canvas.drawText(festival, dayRang.centerX(),
                        dayRang.top +
                                firstTopMargin + Math.abs(dayFirstAscent), firstSelectPaint);
            }
            dayPaint.getFontMetrics(fm);
            canvas.drawText(dayText, dayRang.centerX(),
                    dayRang.top +
                            firstTopMargin + dayFirstHeight + secondTopMargin +
                            Math.abs(fm.ascent), dayPaint);
            if (columnNum - dayOffset == 1) {
                top += (rowHeight + dividerHeight);
                if (0 != dividerHeight) {
                    drawDivider(canvas);
                }
            }
        }
    }

    /**
     * 绘制底部选中文案
     *
     * @param canvas
     * @param selectDayText
     */
    private void drawSelectDayText(Canvas canvas, String selectDayText) {
        firstSelectPaint.getFontMetrics(fm);
        thirdPaint.getFontMetrics(fm);
        thirdPaint.setColor(selectTextColor);
        float firstAscent = fm.ascent;
        float firstDescent = fm.descent;
        float firstHeight = firstDescent - firstAscent;
        dayPaint.getFontMetrics(fm);
        float daySecondAscent = fm.ascent;
        float daySecondDescent = fm.descent;
        float daySecondHeight = daySecondDescent - daySecondAscent;
        thirdPaint.getFontMetrics(fm);
        canvas.drawText(selectDayText, dayRang.centerX(), dayRang.top + firstTopMargin + firstHeight +
                        secondTopMargin + daySecondHeight + thirdTopMargin + Math.abs(fm.ascent),
                thirdPaint);
    }

    private void drawSelectRange(Canvas canvas, Rect dayRang, Paint selectPaint) {
        switch (selectStyle) {
            case 0:
                canvas.drawRect(dayRang, selectPaint);
                break;
            case 1:
                RectF rectF = new RectF(dayRang);
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, selectPaint);
                break;
        }
    }

    private void drawDivider(Canvas canvas) {
        canvas.drawRect(getPaddingLeft(), dayRang.bottom, getWidth() - getPaddingRight(), dayRang.bottom + dividerHeight, dividerPaint);
    }


    private boolean isFirstMonth() {
        return firstYear == year && firstMonth == month;
    }


    private boolean isLastMonth() {
        return lastYear == year && lastMonth == month;
    }

    private void drawRange(Canvas canvas) {
        if (isFirstMonth() && isLastMonth()) {
            int firstRangeTop = getFirstRangeTop() + getPaddingTop();
            for (int day = firstDay + 1; day < lastDay; day++) {
                int dayOffset = findDayOffset(firstYear, firstMonth, day);
                int offset = dayOffset * dayWidth + getPaddingLeft();
                selectRangeRect.set(offset, firstRangeTop, offset + dayWidth, firstRangeTop + rowHeight);
                selectPaint.setColor(selectRangeBgColor);
                drawSelectRange(canvas, selectRangeRect, selectPaint);
                if (columnNum - dayOffset == 1) {
                    firstRangeTop += (rowHeight + dividerHeight);
                }
            }
        } else {
            if (isFirstMonth()) {
                Calendar calendar = this.calendar;
                calendar.set(Calendar.YEAR, firstYear);
                calendar.set(Calendar.MONTH, firstMonth);
                int firstMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                int firstRangeTop = getFirstRangeTop() + getPaddingTop();
                for (int day = firstDay + 1; day <= firstMonthMaxDay; day++) {
                    int dayOffset = findDayOffset(firstYear, firstMonth, day);
                    int offset = dayOffset * dayWidth + getPaddingLeft();
                    selectRangeRect.set(offset, firstRangeTop, offset + dayWidth, firstRangeTop + rowHeight);
                    selectPaint.setColor(selectRangeBgColor);
                    drawSelectRange(canvas, selectRangeRect, selectPaint);
                    if (columnNum - dayOffset == 1) {
                        firstRangeTop += (rowHeight + dividerHeight);
                    }
                }
            }
            if (isLastMonth()) {
                int lastRangeTop = getPaddingTop();
                for (int day = 1; day < lastDay; day++) {
                    int dayOffset = findDayOffset(lastYear, lastMonth, day);
                    int offset = dayOffset * dayWidth + getPaddingLeft();
                    selectRangeRect.set(offset, lastRangeTop, offset + dayWidth, lastRangeTop + rowHeight);
                    selectPaint.setColor(selectRangeBgColor);
                    drawSelectRange(canvas, selectRangeRect, selectPaint);
                    if (columnNum - dayOffset == 1) {
                        lastRangeTop += (rowHeight + dividerHeight);
                    }
                }
            }
        }
    }


    private int getFirstRangeTop() {
        int offset = findDayOffset(firstYear, firstMonth, 1);
        int dividend = (offset + firstDay) / columnNum;
        return dividend * (rowHeight + dividerHeight);
    }


    private boolean isSameDay(int day) {
        return year == currentYear && month == currentMonth && day == currentDay;
    }

    private boolean isPreDay(int day) {
        return year <= currentYear && month <= currentMonth
                && day < currentDay;
    }


    private boolean isMaxRange(int day) {
        if (firstDay == -1 || selectMaxRange == -1) {
            return false;
        }
        return (dayOfYear + day - firstDayOfYear) > selectMaxRange;
    }


    public void setParams(Map<String, Integer> params) {
        year = params.get(VIEW_YEAR);
        month = params.get(VIEW_MONTH);
        covertAndSaveFestival();
        if (params.containsKey(VIEW_FIRST_SELECT_YEAR)) {
            firstYear = params.get(VIEW_FIRST_SELECT_YEAR);
        }
        if (params.containsKey(VIEW_FIRST_SELECT_MONTH)) {
            firstMonth = params.get(VIEW_FIRST_SELECT_MONTH);
        }
        if (params.containsKey(VIEW_FIRST_SELECT_DAY)) {
            firstDay = params.get(VIEW_FIRST_SELECT_DAY);
        }
        if (params.containsKey(VIEW_LAST_SELECT_YEAR)) {
            lastYear = params.get(VIEW_LAST_SELECT_YEAR);
        }
        if (params.containsKey(VIEW_LAST_SELECT_MONTH)) {
            lastMonth = params.get(VIEW_LAST_SELECT_MONTH);
        }
        if (params.containsKey(VIEW_LAST_SELECT_DAY)) {
            lastDay = params.get(VIEW_LAST_SELECT_DAY);
        }

        if (firstDay != -1) {
            Calendar calendar = this.calendar;
            calendar.set(Calendar.YEAR, firstYear);
            calendar.set(Calendar.MONTH, firstMonth);
            calendar.set(Calendar.DATE, firstDay);
            firstDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DATE, 1);
            dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        }

        if (lastDay != -1) {
            Calendar calendar = this.calendar;
            calendar.set(Calendar.YEAR, lastYear);
            calendar.set(Calendar.MONTH, lastMonth);
            calendar.set(Calendar.DATE, lastDay);
            lastDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        }

        rowNum = calculateNumRows();
        requestLayout();
    }

    private void covertAndSaveFestival() {
        festivalMap.clear();
        solar.reset();
        solar.solarYear = year;
        solar.solarMonth = month + 1;
        int dayOfMonth = dayOfMonth(year, month);
        for (int day = 1; day <= dayOfMonth; day++) {
            solar.solarDay = day;
            Lunar lunar = LunarSolarConverter.SolarToLunar(solar);
            String festival = solar.isSFestival ? solar.solarFestivalName : lunar.isLFestival ?
                    lunar.lunarFestivalName : "";
            if (!TextUtils.isEmpty(festival)) {
                festivalMap.put(day, festival);
            }
            solar.isSFestival = false;
            solar.solar24Term = "";
            solar.solarFestivalName = "";
        }
    }


    private boolean isFirstDay(int day) {
        return year == firstYear && month == firstMonth && day == firstDay;
    }


    private boolean isLastDay(int day) {
        return year == lastYear && month == lastMonth && day == lastDay;
    }

    public int dayOfMonth(int year, int month) {
        Calendar calendar = this.calendar;
        calendar.set(year, month + 1, 0);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    private int calculateNumRows() {
        int offset = findDayOffset(year, month, 1);
        int days = dayOfMonth(year, month);
        int dividend = (offset + days) / columnNum;
        int remainder = (offset + days) % columnNum;
        return (dividend + (remainder > 0 ? 1 : 0));
    }


    public int findDayOffset(int year, int month, int day) {
        Calendar calendar = this.calendar;
        calendar.set(year, month, day);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }


    public interface OnDayClickListener {
        void onDayClick(CalendarDay calendarDay);
    }
}
