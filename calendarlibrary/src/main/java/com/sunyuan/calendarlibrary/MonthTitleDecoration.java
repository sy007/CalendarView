package com.sunyuan.calendarlibrary;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class MonthTitleDecoration extends RecyclerView.ItemDecoration {

    private boolean isInitHeight;
    private int monthTitleHeight;
    private Map<Integer, View> monthTitleViewMap = new HashMap<>();
    private MonthTitleViewCallBack monthTitleViewCallBack;

    public void setMonthTitleViewCallBack(MonthTitleViewCallBack monthTitleViewCallBack) {
        this.monthTitleViewCallBack = monthTitleViewCallBack;
    }

    public interface MonthDateCallback {
        Date getMonthDate(int position);
    }

    public MonthTitleDecoration() {

    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof MonthDateCallback) {
            MonthDateCallback monthDateCallback = (MonthDateCallback) adapter;
            if (!isInitHeight && monthTitleViewCallBack != null) {
                Date monthDate = monthDateCallback.getMonthDate(0);
                View monthTitleView = monthTitleViewCallBack.getMonthTitleView(0, monthDate);
                monthTitleView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                monthTitleHeight = monthTitleView.getMeasuredHeight();
                isInitHeight = true;
            }
        }
        //预留MonthTitleView的高度
        outRect.top = monthTitleHeight;
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof MonthDateCallback) {
            int childCount = parent.getChildCount();
            MonthDateCallback monthDateCallback = (MonthDateCallback) parent.getAdapter();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = parent.getPaddingTop();
            /**
             * 粘性思路
             *
             * 1.当最后一个itemView bottom与monthTitle bottom重合时候。第二个monthTitle就到了第一个monthTitle底部
             * 2.当重合后再往上滑动时，只需要不断改变悬停monthTitle top值(itemView.bottom-monthTitleHeight)重新绘制
             * 从而视觉上感觉 第二个monthTitle往上推第一个monthTitle。
             */
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                int index = parent.getChildAdapterPosition(view);
                View monthTitleView;
                if (monthTitleViewMap.get(index) == null && monthTitleViewCallBack != null) {
                    Date monthDate = monthDateCallback.getMonthDate(index);
                    monthTitleView = monthTitleViewCallBack.getMonthTitleView(index, monthDate);
                    monthTitleView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    monthTitleViewMap.put(index, monthTitleView);
                    monthTitleView.layout(0, 0, parent.getWidth(), monthTitleHeight);
                } else {
                    monthTitleView = monthTitleViewMap.get(index);
                }
                if (i == 0) {
                    int tempTop = view.getBottom() - monthTitleHeight;
                    if (tempTop < top) {
                        top = tempTop;
                    }
                } else {
                    top = view.getTop() - monthTitleHeight;
                }
                c.save();
                c.translate(0, top);
                c.clipRect(left, 0, right, monthTitleHeight);
                monthTitleView.draw(c);
                c.restore();
            }
        }
    }

    /**
     * 释放资源
     */
    public void destroy() {
        monthTitleViewMap.clear();
        monthTitleViewCallBack = null;
    }
}
