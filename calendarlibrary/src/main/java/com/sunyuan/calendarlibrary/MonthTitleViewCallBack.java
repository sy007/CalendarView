package com.sunyuan.calendarlibrary;

import android.view.View;

import java.util.Date;

/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public interface MonthTitleViewCallBack {
    /**
     * 提供外部设置MonthTitleView
     *
     * @param position adapter的position位置
     * @param date     月份日期
     * @return
     */
    View getMonthTitleView(int position, Date date);
}
