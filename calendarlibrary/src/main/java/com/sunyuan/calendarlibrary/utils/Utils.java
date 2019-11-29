package com.sunyuan.calendarlibrary.utils;

import android.content.Context;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

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

    /**
     * 获取两个日期月数差
     *
     * @param start
     * @param end
     * @return
     */
    public static int getMonthDiff(Calendar start, Calendar end) {
        int yearEnd = end.get(Calendar.YEAR);
        int yearStart = start.get(Calendar.YEAR);
        int monthEnd = end.get(Calendar.MONTH);
        int monthStart = start.get(Calendar.MONTH);
        if (yearStart == yearEnd && monthStart == monthEnd) {
            return 0;
        }
        int yearInterval = yearEnd - yearStart;
        if (monthEnd < monthStart) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (monthEnd + 12) - monthStart;
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

    /**
     * 获取两个日期 天数差
     *
     * @param calendar1
     * @param calendar2
     * @return
     */
    public static int getOffectDay(Calendar calendar1, Calendar calendar2) {
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays;
        int day;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = Math.abs(d1 - d2);
        }
        return day;
    }
}
