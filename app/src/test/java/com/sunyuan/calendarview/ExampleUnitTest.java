package com.sunyuan.calendarview;

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
        Lunar lunarCalendar = new Lunar();
        Solar solar = new Solar();
        lunarCalendar.lunarYear =2018;
        lunarCalendar.lunarMonth= 12;
        lunarCalendar.lunarDay =30;
        solar.solarYear =2019;
        solar.solarMonth =1;
        solar.solarDay=1;
        LunarSolarConverter lunarSolarConverter =new LunarSolarConverter();

        System.out.println("SUNYUAN" +lunarSolarConverter.SolarToLunar(solar));
        System.out.println("-------------------------------------------------------------------");
        System.out.println("SUNYUAN" +"LunarToSolar:"+ lunarSolarConverter.LunarToSolar(lunarCalendar).toString());

        System.out.println("-------------------------------------------------------------------");
        System.out.println("SUNYUAN" +"SolarToLunar:"+ lunarSolarConverter.SolarToLunar(solar).toString());
    }
}