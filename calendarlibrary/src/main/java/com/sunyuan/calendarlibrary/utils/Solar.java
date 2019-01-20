package com.sunyuan.calendarlibrary.utils;

public class Solar {
    public int solarDay;
    public int solarMonth;
    public int solarYear;
    public boolean isSFestival;
    public String solarFestivalName;//公历节日
    public String solar24Term;//24节气

    @Override
    public String toString() {

        return "Solar [solarDay=" + solarDay + ", solarMonth=" + solarMonth

                + ", solarYear=" + solarYear + ", isSFestival=" + isSFestival

                + ", solarFestivalName=" + solarFestivalName + ", solar24Term="

                + solar24Term + "]";

    }

    public void reset() {
        solarDay = 0;
        solarMonth = 0;
        solarYear = 0;
        isSFestival = false;
        solarFestivalName = "";
        solar24Term = "";
    }
}
