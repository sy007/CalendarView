package com.sunyuan.calendarlibrary.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class CalendarDay implements Serializable {

    private int year;
    private int month;
    private int day;

    public CalendarDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }


    public Date toDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        return calendar.getTime();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarDay)) return false;
        CalendarDay that = (CalendarDay) o;
        return getYear() == that.getYear() &&
                getMonth() == that.getMonth() &&
                getDay() == that.getDay();
    }

    @Override
    public int hashCode() {
        return hashCode(getYear(), getMonth(), getDay());
    }

    public static int hashCode(Object... values) {
        if (values == null)
            return 0;
        int result = 1;
        for (Object element : values)
            result = 31 * result + (element == null ? 0 : element.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "CalendarBean{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
