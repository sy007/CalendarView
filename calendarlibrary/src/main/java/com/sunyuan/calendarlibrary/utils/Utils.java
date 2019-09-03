package com.sunyuan.calendarlibrary.utils;

import android.content.Context;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        int endDayInt = end.get(Calendar.DAY_OF_MONTH);
        int startDayInt = start.get(Calendar.DAY_OF_MONTH);
        if (yearStart == yearEnd && monthStart == monthEnd) {
            return 0;
        }
        int yearInterval = yearEnd - yearStart;
        if (monthEnd < monthStart) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (monthEnd + 12) - monthStart;
        if (endDayInt < startDayInt) {
            monthInterval--;
        }
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

    /**
     * 获取两个日期 天数差
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDayDiff(Calendar start, Calendar end) {
        int startDay = start.get(Calendar.DAY_OF_YEAR);
        int endDay = end.get(Calendar.DAY_OF_YEAR);
        int startYear = start.get(Calendar.YEAR);
        int endYear = end.get(Calendar.YEAR);
        if (startYear != endYear) {
            int timeDistance = 0;
            for (int i = startYear; i < endYear; i++) {
                //闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    //不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (endDay - startDay);
        } else {
            //同一年
            return endDay - startDay;
        }
    }
}
