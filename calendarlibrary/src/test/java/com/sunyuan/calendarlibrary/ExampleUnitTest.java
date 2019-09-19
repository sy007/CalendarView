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

    }

    public static void main(String[] a) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 13);
        int monthDiff = Utils.getMonthDiff(start, end);
        System.out.println(monthDiff);
    }

}