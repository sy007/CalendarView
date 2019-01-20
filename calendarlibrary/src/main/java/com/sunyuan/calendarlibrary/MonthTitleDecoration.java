package com.sunyuan.calendarlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sunyuan.calendarlibrary.utils.Utils;

/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class MonthTitleDecoration extends RecyclerView.ItemDecoration {

    private int monthTitleHeight;
    private final Paint paint;
    private final Rect rect;
    private final Paint.FontMetrics fontMetrics;
    private final int monthTitleBgColor;
    private final int monthTitleTextColor;

    public interface MonthTitleCallback {
        String getMonthTitle(int position);
    }


    public MonthTitleDecoration(Context context, TypedArray typedArray) {
        this.monthTitleHeight = (int) typedArray.getDimension(R.styleable.CalendarView_monthTitleHeight, Utils.dip2px(context,40));
        this.monthTitleBgColor = typedArray.getColor(R.styleable.CalendarView_monthTitleBgColor, Color.parseColor("#BBBBBB"));
        this.monthTitleTextColor = typedArray.getColor(R.styleable.CalendarView_monthTitleTextColor, Color.parseColor("#000000"));
        float monthTitleTextSize = typedArray.getDimension(R.styleable.CalendarView_monthTitleTextSize, Utils.sp2px(context,13));
        rect = new Rect();
        fontMetrics = new Paint.FontMetrics();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(monthTitleTextSize);
        paint.getFontMetrics(fontMetrics);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //预留Header的高度
        outRect.top = monthTitleHeight;
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        MonthTitleCallback monthTitleCallback = (MonthTitleCallback) parent.getAdapter();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top;
        int bottom;
        /**
         * 悬停部分思路
         *
         * 当前屏幕上第一个可见itemView。绘制monthTitle时保持monthTitle在RecyclView最上方。
         * 当后面的itemView向上滑动时,又会变成屏幕上第一个可见的itemView。从而视觉上感觉
         * monthTitle一直在悬停。
         *
         * 粘性思路
         *
         * 1.当最后一个itemView bottom与monthTitle bottom重合时候。第二个monthTitle就到了第一个monthTitle底部
         * 2.当重合后再往上滑动时，只需要不断改变悬停monthTitle top值(itemView.bottom-monthTitleHeight)重新绘制
         * 从而视觉上感觉 第二个monthTitle往上推第一个monthTitle。
         */
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(view);
            String monthTitle = monthTitleCallback.getMonthTitle(index);
            if (i == 0) {
                top = parent.getPaddingTop();
                int tempTop = view.getBottom() - monthTitleHeight;
                if (tempTop < top) {
                    top = tempTop;
                }
                bottom = top + monthTitleHeight;
            } else {
                top = view.getTop() - monthTitleHeight;
                bottom = view.getTop();
            }
            drawMonthTitle(c, left, right, top, bottom, monthTitle);
        }
    }


    private void drawMonthTitle(@NonNull Canvas c, int left, int right, int top, int bottom, String groupName) {
        paint.setColor(monthTitleBgColor);
        rect.set(left, top, right, bottom);
        c.drawRect(rect, paint);
        float distance = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
        float baseline = rect.centerY() + distance;
        paint.setColor(monthTitleTextColor);
        c.drawText(groupName, rect.centerX(), baseline, paint);
    }
}
