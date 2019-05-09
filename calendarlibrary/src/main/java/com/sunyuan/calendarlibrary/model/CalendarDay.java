package com.sunyuan.calendarlibrary.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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
                getMonth() == that.getMonth();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getMonth());
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
