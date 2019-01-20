package com.sunyuan.calendarlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class MonthLableView extends View {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("EEEEE");
    public static final Map<String, Object> ATTRS = new HashMap<>();
    public static final String LABLE_WEEKEND_TEXT_COLOR = "LABLE_WEEKEND_TEXT_COLOR";
    public static final String LABLE_TEXT_COLOR = "LABLE_TEXT_COLOR";
    public static final String LABLE_TEXT_SIZE = "LABLE_TEXT_SIZE";
    public static final String LABLE_PADDING_LEFT = "LABLE_PADDING_LEFT";
    public static final String LABLE_PADDING_TOP = "LABLE_PADDING_TOP";
    public static final String LABLE_PADDING_RIGHT = "LABLE_PADDING_RIGHT";
    public static final String LABLE_PADDING_BOTTOM = "LABLE_PADDING_BOTTOM";

    private int weekendTextColor;
    private int textColor;
    private int textSize;
    private Paint.FontMetrics fontMetrics;
    private Paint lablePaint;
    private Calendar calendar;
    private int columnNum = 7;
    private int lableWidht;
    private Rect lableRect;

    public MonthLableView(Context context) {
        this(context, null);
    }

    public MonthLableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MonthLableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        calendar = Calendar.getInstance();
        weekendTextColor = (int) ATTRS.get(LABLE_WEEKEND_TEXT_COLOR);
        textColor = (int) ATTRS.get(LABLE_TEXT_COLOR);
        textSize = (int) ATTRS.get(LABLE_TEXT_SIZE);
        int paddingLeft = (int) ATTRS.get(LABLE_PADDING_LEFT);
        int paddingTop = (int) ATTRS.get(LABLE_PADDING_TOP);
        int paddingRight = (int) ATTRS.get(LABLE_PADDING_RIGHT);
        int paddingBottom = (int) ATTRS.get(LABLE_PADDING_BOTTOM);
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        initPaint();
    }

    private void initPaint() {
        lableRect = new Rect();
        lablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lablePaint.setTextAlign(Paint.Align.CENTER);
        lablePaint.setTextSize(textSize);
        fontMetrics = new Paint.FontMetrics();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lablePaint.getFontMetrics(fontMetrics);
        setMeasuredDimension(widthMeasureSpec, (int) (fontMetrics.descent - fontMetrics.ascent) + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        lableWidht = (w - getPaddingLeft() - getPaddingRight()) / columnNum;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        lablePaint.getFontMetrics(fontMetrics);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        for (int i = 0; i < columnNum; i++) {
            lableRect.set(left, top, left + lableWidht, top + lableHeight);
            if (i == 0 || i == columnNum - 1) {
                lablePaint.setColor(weekendTextColor);
            } else {
                lablePaint.setColor(textColor);
            }
            int calendarDay = (i + calendar.getFirstDayOfWeek()) % columnNum;
            calendar.set(Calendar.DAY_OF_WEEK, calendarDay);
            String dayLabelText = SIMPLE_DATE_FORMAT.format(calendar.getTime());
            float distance = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
            float baseline = lableRect.centerY() + distance;
            canvas.drawText(dayLabelText, lableRect.centerX(), baseline, lablePaint);
            left += lableWidht;
        }
    }

}
