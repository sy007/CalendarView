package com.sunyuan.calendarlibrary;

import com.sunyuan.calendarlibrary.utils.Utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() {
        Calendar firstCalendar = Calendar.getInstance();
        firstCalendar.set(Calendar.YEAR, 2020);
        firstCalendar.set(Calendar.MONTH, 0);
        firstCalendar.set(Calendar.DATE, 1);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.YEAR, 2019);
        currentCalendar.set(Calendar.MONTH, 11);
        currentCalendar.set(Calendar.DATE, 1);
        //第一次选中日期到当前展示月份的差
        System.out.println(getOffectDay(firstCalendar, currentCalendar));
    }

    /**
     * 描述：计算两个日期所差的天数.
     *
     * @return int 所差的天数
     */
    public static int getOffectDay(Calendar calendar1, Calendar calendar2) {
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

}