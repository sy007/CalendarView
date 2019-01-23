package com.sunyuan.calendarlibrary;

import android.view.View;

import java.util.Date;

/**
 * author:Six
 * Date:2019/1/23
 */
public interface MonthTitleViewCallBack {
    View getMonthTitleView(int position, Date date);
}
