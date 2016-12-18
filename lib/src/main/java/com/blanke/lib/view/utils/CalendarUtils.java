package com.blanke.lib.view.utils;

import java.util.Calendar;

/**
 * Created by blanke on 16-12-14.
 */

public class CalendarUtils {
    public static Calendar getToDay() {
        Calendar toDay = Calendar.getInstance();
        int year = toDay.get(Calendar.YEAR);
        int month = toDay.get(Calendar.MONTH);
        int day = toDay.get(Calendar.DAY_OF_MONTH);
        toDay.clear();
        toDay.set(Calendar.YEAR, year);
        toDay.set(Calendar.MONTH, month);
        toDay.set(Calendar.DAY_OF_MONTH, day);
        return toDay;
    }

    public static Calendar getCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    //或者这个月的天数
    public static int getMaxMonthCount(Calendar monthDay) {
        return monthDay.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
