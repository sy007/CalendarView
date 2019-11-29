package com.sunyuan.calendarlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.utils.Lunar;
import com.sunyuan.calendarlibrary.utils.LunarSolarConverter;
import com.sunyuan.calendarlibrary.utils.Solar;
import com.sunyuan.calendarlibrary.utils.Utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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
    private int toYear;
    private int toMonth;
    private int toDay;
    private Rect dayRang;
    private static final int MONTH_IN_YEAR = 12;
    private static final String MEASURE_TEXT = "\u5b57";


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
    public static final String SELECT_BG_DRAWABLE = "SELECT_BG_DRAWABLE";
    public static final String SELECT_RANGE_BG_DRAWABLE = "SELECT_RANGE_BG_DRAWABLE";
    public static final String WEEKEND_TEXT_COLOR = "WEEKEND_TEXT_COLOR";
    public static final String DIS_TEXT_COLOR = "DIS_TEXT_COLOR";
    public static final String SAME_TEXT_COLOR = "SAME_TEXT_COLOR";
    public static final String SELECT_MAX_RANGE = "SELECT_MAX_RANGE";
    public static final String DIVIDER_HEIGHT = "DIVIDER_HEIGHT";
    public static final String DIVIDER_COLOR = "DIVIDER_COLOR";
    public static final String TOP_SIZE = "TOP_SIZE";
    public static final String TEXT_SIZE = "TEXT_SIZE";
    public static final String TEXT_STYLE = "TEXT_STYLE";
    public static final String BOTTOM_TEXT_SIZE = "BOTTOM_TEXT_SIZE";
    public static final String FIRST_TOP_MARGIN = "FIRST_TOP_MARGIN";
    public static final String SECOND_TOP_MARGIN = "SECOND_TOP_MARGIN";
    public static final String THIRD_TOP_MARGIN = "THIRD_TOP_MARGIN";
    public static final String ROW_HEIGHT = "ROW_HEIGHT";
    public static final String MONTH_PADDING_LEFT = "MONTH_PADDING_LEFT";
    public static final String MONTH_PADDING_TOP = "MONTH_PADDING_TOP";
    public static final String MONTH_PADDING_RIGHT = "MONTH_PADDING_RIGHT";
    public static final String MONTH_PADDING_BOTTOM = "MONTH_PADDING_BOTTOM";
    public static final String FIRST_SELECT_DAY_TEXT = "FIRST_SELECT_DAY_TEXT";
    public static final String LAST_SELECT_DAY_TEXT = "LAST_SELECT_DAY_TEXT";
    public static final String SELECTION_MODE = "SELECTION_MODE";
    public static final String MIN_DATE = "MIN_DATE";
    public static final String MAX_DATE = "MAX_DATE";

    public static final int DEFAULT_TOP_TEXT_COLOR = Color.parseColor("#FF6E00");
    public static final int DEFAULT_TEXT_COLOR = Color.parseColor("#000000");
    public static final int DEFAULT_SELECT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    public static final int DEFAULT_WEEKEND_TEXT_COLOR = Color.parseColor("#FF6E00");
    public static final int DEFAULT_DIS_TEXT_COLOR = Color.parseColor("#BBBBBB");
    public static final int DEFAULT_SAME_TEXT_COLOR = Color.parseColor("#FF6E00");
    public static final int DEFAULT_SELECT_MAX_RANGE = 0;
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

    private int weekendTextColor;
    private int disTextColor;
    private int bottomTextSize;
    private int textSize;
    private int textStyle;
    private int topTextSize;
    private int firstTopMargin;
    private int secondTopMargin;
    private int sameTextColor;
    private Paint.FontMetrics fm;
    private Rect selectRangeRect;
    private Map<Integer, String> festivalMap = new HashMap<>();
    private Solar solar;
    private Paint firstSelectPaint;
    private int topTextColor;
    private int thirdTopMargin;
    private int dividerHeight;
    private int dividerColor;
    private Paint dividerPaint;
    private String firstSelectDayText;
    private String lastSelectDayText;
    private SelectionMode selectionMode;


    private Calendar minCalendar;
    private Calendar maxCalendar;

    /**
     * 可选择的最大范围天数，只有选择模式为范围选择时生效。
     * 比如当选择模式为范围选择时，selectMaxRange =10 当选中第一个日期后 2019-9-3，那么第二个日期可以选择范围为2019-9-4到2019-9-13。
     */
    private int selectMaxRange;
    private int curToFirstDayDiff;
    private Drawable selectBgDrawable;
    private Drawable selectRangeDrawable;

    /**
     * 用于放置日期字体高度
     */
    private Rect textHeightRect = new Rect();


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
        if (ATTRS.get(SELECT_BG_DRAWABLE) != null) {
            selectBgDrawable = (Drawable) ATTRS.get(SELECT_BG_DRAWABLE);
        }
        if (ATTRS.get(SELECT_RANGE_BG_DRAWABLE) != null) {
            selectRangeDrawable = (Drawable) ATTRS.get(SELECT_RANGE_BG_DRAWABLE);
        }
        weekendTextColor = (int) ATTRS.get(WEEKEND_TEXT_COLOR);
        disTextColor = (int) ATTRS.get(DIS_TEXT_COLOR);
        topTextColor = (int) ATTRS.get(TOP_TEXT_COLOR);
        sameTextColor = (int) ATTRS.get(SAME_TEXT_COLOR);
        topTextSize = (int) ATTRS.get(TOP_SIZE);
        textSize = (int) ATTRS.get(TEXT_SIZE);
        textStyle = (int) ATTRS.get(TEXT_STYLE);
        bottomTextSize = (int) ATTRS.get(BOTTOM_TEXT_SIZE);
        firstTopMargin = (int) ATTRS.get(FIRST_TOP_MARGIN);
        secondTopMargin = (int) ATTRS.get(SECOND_TOP_MARGIN);
        thirdTopMargin = (int) ATTRS.get(THIRD_TOP_MARGIN);
        selectMaxRange = (int) ATTRS.get(SELECT_MAX_RANGE);
        dividerHeight = (int) ATTRS.get(DIVIDER_HEIGHT);
        dividerColor = (int) ATTRS.get(DIVIDER_COLOR);
        rowHeight = (int) ATTRS.get(ROW_HEIGHT);
        firstSelectDayText = (String) ATTRS.get(FIRST_SELECT_DAY_TEXT);
        lastSelectDayText = (String) ATTRS.get(LAST_SELECT_DAY_TEXT);
        selectionMode = (SelectionMode) ATTRS.get(SELECTION_MODE);
        int paddingLeft = (int) ATTRS.get(MONTH_PADDING_LEFT);
        int paddingTop = (int) ATTRS.get(MONTH_PADDING_TOP);
        int paddingRight = (int) ATTRS.get(MONTH_PADDING_RIGHT);
        int paddingBottom = (int) ATTRS.get(MONTH_PADDING_BOTTOM);
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        //获取最小日期和最大日期
        minCalendar = (Calendar) ATTRS.get(MIN_DATE);
        maxCalendar = (Calendar) ATTRS.get(MAX_DATE);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        toYear = calendar.get(Calendar.YEAR);
        toMonth = calendar.get(Calendar.MONTH);
        toDay = calendar.get(Calendar.DATE);
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
        int flags = Paint.ANTI_ALIAS_FLAG;
        switch (textStyle) {
            case 0:
                dayPaint.setFlags(flags);
                break;
            case 1:
                dayPaint.setFlags(flags | Paint.FAKE_BOLD_TEXT_FLAG);
                break;
        }
        dividerPaint = new Paint();
        dividerPaint.setColor(dividerColor);

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
        //这里宽度存在除不尽情况
        dayWidth = ((w - (getPaddingLeft() + getPaddingRight())) / columnNum);
        //将多余的宽度平均分给两边的padding
        int halfRemainWidth = ((w - getPaddingLeft() - getPaddingRight()) % columnNum) / 2;
        setPadding(getPaddingLeft() + halfRemainWidth, getPaddingTop(), getPaddingRight() + halfRemainWidth, getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDays(canvas);
        if (SelectionMode.RANGE == selectionMode && lastDay != -1) {
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

        if (dayOfMonth(year, month) < day || day < 1 || isPreDay(day) || isMaxSelectDay(day) ||
                isOverMaxRange(day)) {
            return null;
        }

        return new CalendarDay(year, month, day);
    }


    private void drawDays(Canvas canvas) {
        String dayText;
        int dayOfMonth = dayOfMonth(year, month);
        int paddingLeft = getPaddingLeft();
        int top = getPaddingTop();
        for (int day = 1; day <= dayOfMonth; day++) {
            String festival = festivalMap.get(day);
            int dayOffset = findDayOffset(year, month, day);
            int offset = dayOffset * dayWidth + paddingLeft;
            dayRang.set(offset, top, offset + dayWidth, top + rowHeight);
            if (isFirstDay(day) || isLastDay(day)) {
                if (selectBgDrawable != null) {
                    selectBgDrawable.setBounds(dayRang.left, dayRang.top, dayRang.right, dayRang.bottom);
                    selectBgDrawable.draw(canvas);
                }
            }
            dayText = String.format(Locale.CANADA, "%d", day);
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
                if (isPreDay(day) || isMaxSelectDay(day) || isOverMaxRange(day)) {
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
            firstSelectPaint.getFontMetrics(fm);
            float dayFirstHeight = fm.descent - fm.ascent;
            if (!TextUtils.isEmpty(festival)) {
                canvas.drawText(festival, dayRang.centerX(),
                        dayRang.top + firstTopMargin + Math.abs(fm.ascent), firstSelectPaint);
            }
            dayPaint.getFontMetrics(fm);
            canvas.drawText(dayText, dayRang.centerX(), dayRang.top +
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


    private boolean isMaxSelectDay(int day) {
        //如果最小限制的日期和最大限制的日期在同一月，可选择范围在最小限制日期到最大限制日期
        if (isMinMonth() && isMaxMonth()) {
            int minDay = minCalendar.get(Calendar.DATE);
            int maxDay = maxCalendar.get(Calendar.DATE);
            return !(day >= minDay && day <= maxDay);
        }
        //如果当前显示在最小限制日期，可选择范围在最小限制日期到该时间月底
        if (isMinMonth()) {
            int minDay = minCalendar.get(Calendar.DATE);
            int monthOfDay = minCalendar.getActualMaximum(Calendar.DATE);
            return !(day >= minDay && day <= monthOfDay);
        }
        //如果当前显示在最大限制日期，可选择范围在月初到最大限制日期
        if (isMaxMonth()) {
            int maxDay = maxCalendar.get(Calendar.DATE);
            return !(day <= maxDay);
        }
        return false;
    }


    private boolean isMinMonth() {
        int minYear = minCalendar.get(Calendar.YEAR);
        int minMonth = minCalendar.get(Calendar.MONTH);
        return (minYear == year && minMonth == month);
    }


    private boolean isMaxMonth() {
        int maxYear = maxCalendar.get(Calendar.YEAR);
        int maxMonth = maxCalendar.get(Calendar.MONTH);
        return (maxYear == year && maxMonth == month);
    }

    /**
     * 绘制底部选中文案
     *
     * @param canvas
     * @param selectDayText
     */
    private void drawSelectDayText(Canvas canvas, String selectDayText) {
        firstSelectPaint.getFontMetrics(fm);
        float firstAscent = fm.ascent;
        float firstDescent = fm.descent;
        float firstHeight = firstDescent - firstAscent;
        dayPaint.getFontMetrics(fm);
        float daySecondAscent = fm.ascent;
        float daySecondDescent = fm.descent;
        float daySecondHeight = daySecondDescent - daySecondAscent;
        thirdPaint.getFontMetrics(fm);
        thirdPaint.setColor(selectTextColor);
        canvas.drawText(selectDayText, dayRang.centerX(), dayRang.top + firstTopMargin + firstHeight +
                        secondTopMargin + daySecondHeight + thirdTopMargin + Math.abs(fm.ascent),
                thirdPaint);
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
        //选择同一个月
        if (isFirstMonth() && isLastMonth()) {
            int firstRangeTop = getFirstRangeTop() + getPaddingTop();
            for (int day = firstDay + 1; day < lastDay; day++) {
                int dayOffset = findDayOffset(firstYear, firstMonth, day);
                int offset = dayOffset * dayWidth + getPaddingLeft();
                selectRangeRect.set(offset, firstRangeTop, offset + dayWidth, firstRangeTop + rowHeight);
                drawSelectRange(canvas, selectRangeRect);
                if (columnNum - dayOffset == 1) {
                    firstRangeTop += (rowHeight + dividerHeight);
                }
            }
        } else {
            //选择不同月
            //绘制第一次选择的月份 选中范围
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
                    drawSelectRange(canvas, selectRangeRect);
                    if (columnNum - dayOffset == 1) {
                        firstRangeTop += (rowHeight + dividerHeight);
                    }
                }
            }
            //绘制最后一次选择的月份 选中范围
            if (isLastMonth()) {
                int lastRangeTop = getPaddingTop();
                for (int day = 1; day < lastDay; day++) {
                    int dayOffset = findDayOffset(lastYear, lastMonth, day);
                    int offset = dayOffset * dayWidth + getPaddingLeft();
                    selectRangeRect.set(offset, lastRangeTop, offset + dayWidth, lastRangeTop + rowHeight);
                    drawSelectRange(canvas, selectRangeRect);
                    if (columnNum - dayOffset == 1) {
                        lastRangeTop += (rowHeight + dividerHeight);
                    }
                }
            }
            //绘制中间月份      选中范围
            if (isDrawSelectRangeOfMiddleStatus()) {
                int top = getPaddingTop();
                Calendar calendar = this.calendar;
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                int monthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int day = 1; day <= monthMaxDay; day++) {
                    int dayOffset = findDayOffset(year, month, day);
                    int offset = dayOffset * dayWidth + getPaddingLeft();
                    selectRangeRect.set(offset, top, offset + dayWidth, top + rowHeight);
                    drawSelectRange(canvas, selectRangeRect);
                    if (columnNum - dayOffset == 1) {
                        top += (rowHeight + dividerHeight);
                    }
                }
            }
        }
    }


    private void drawSelectRange(Canvas canvas, Rect dayRang) {
        if (selectRangeDrawable != null) {
            selectRangeDrawable.setBounds(dayRang);
            selectRangeDrawable.draw(canvas);
        }
    }


    private int getFirstRangeTop() {
        int offset = findDayOffset(firstYear, firstMonth, 1);
        int dividend = (offset + firstDay) / columnNum;
        return dividend * (rowHeight + dividerHeight);
    }


    private boolean isSameDay(int day) {
        return year == toYear && month == toMonth && day == toDay;
    }

    private boolean isPreDay(int day) {
        if (year < toYear) {
            return true;
        }
        if (year == toYear && month < toMonth) {
            return true;
        }
        return year == toYear && month == toMonth && day < toDay;
    }


    private boolean isDrawSelectRangeOfMiddleStatus() {
        int totalMonth = year * MONTH_IN_YEAR + month;
        int firstTotalMonth = firstYear * MONTH_IN_YEAR + firstMonth;
        int lastTotalMonth = lastYear * MONTH_IN_YEAR + lastMonth;
        return totalMonth > firstTotalMonth && totalMonth < lastTotalMonth;
    }


    /**
     * 是否超过选择范围
     *
     * @param day
     * @return
     */
    private boolean isOverMaxRange(int day) {
        if (SelectionMode.SINGLE == selectionMode) {
            return false;
        }
        //如果没有选择日期或者选择范围为0,则当前展示的所有日期可以选择
        if (firstDay == -1 || selectMaxRange == DEFAULT_SELECT_MAX_RANGE) {
            return false;
        }
        //如果当前日期在第一次选中日期之前，那么直接return false。当前日期可以被选择的
        if (year < firstYear) {
            return false;
        }
        if (year == firstYear && month < firstMonth) {
            return false;
        }
        //如果当前日期和第一次选中日期在同一个月，或者比第一次选择日期大，那么此时可以根据两个日期的天数差和可选择的天数范围比较
        //超过则不可选择,没超过则可以选择
        return (curToFirstDayDiff + day - 1) > selectMaxRange;
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
            Calendar firstCalendar = Calendar.getInstance();
            firstCalendar.set(Calendar.YEAR, firstYear);
            firstCalendar.set(Calendar.MONTH, firstMonth);
            firstCalendar.set(Calendar.DATE, firstDay);
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.set(Calendar.YEAR, year);
            currentCalendar.set(Calendar.MONTH, month);
            currentCalendar.set(Calendar.DATE, 1);
            //第一次选中日期到当前展示月份的天数差
            curToFirstDayDiff = Utils.getOffectDay(firstCalendar, currentCalendar);
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
        this.calendar.set(year, month + 1, 0);
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
