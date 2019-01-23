package com.sunyuan.calendarlibrary.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class Utils {
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
