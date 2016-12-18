package com.blanke.lib.view.bean;


import com.blanke.lib.view.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarMonthModel {
    private int monthDayCount;//这个月多少天
    private Calendar monthCalendar;
    private int dayOffset;
    private List<CalendarDayModel> days;
    private boolean hasSelectedStartAndEnd;//是否同时选中首尾
    private int month;
    private int year;


    public CalendarMonthModel(int year, int month) {
        this(CalendarUtils.getCalendar(year, month, 1));
    }

    public CalendarMonthModel(Calendar monthCalendar) {
        this.monthCalendar = monthCalendar;
        this.year = monthCalendar.get(Calendar.YEAR);
        this.month = monthCalendar.get(Calendar.MONTH);
        dayOffset = findDayOffset();
        Calendar toDay = CalendarUtils.getToDay();
        monthDayCount = CalendarUtils.getMaxMonthCount(monthCalendar);//这个月多少天
        days = new ArrayList(monthDayCount);
        for (int i = 1; i <= monthDayCount; i++) {
            Calendar dayCalendar = CalendarUtils.getCalendar(year, month, i);
            CalendarDayModel day = new CalendarDayModel(i, dayCalendar);
            if (toDay.equals(dayCalendar)) {
                day.isToday = true;
            } else if (toDay.after(dayCalendar)) {
                day.isInThePast = true;
            } else {
                day.isInTheFuture = true;
            }
            days.add(day);
        }
    }


    private int findDayOffset() {
        int i = this.monthCalendar.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = this.monthCalendar.getFirstDayOfWeek();
        if (i < firstDayOfWeek) {
            i += 7;
        }
        return i - firstDayOfWeek;
    }

    public int getCurrentMonth() {
        return this.month;
    }

    public int getCurrentYear() {
        return this.year;
    }

    public CalendarDayModel getDayModel(int day) {
        if (day <= 0 || day > this.getNumberDaysInMonth()) {
            return null;
        } else {
            return getDays().get(day - 1);
        }
    }

    public int getDayOffset() {
        return this.dayOffset;
    }

    public List<CalendarDayModel> getDays() {
        return this.days;
    }

    public String getMonthText() {
        return year + "年" + (month + 1) + "月";
    }

    public int getNumberDaysInMonth() {
        return this.days.size();
    }

    public void setHasSelectedStartAndEnd(boolean hasSelectedStartAndEnd) {
        this.hasSelectedStartAndEnd = hasSelectedStartAndEnd;
    }

    public boolean hasSelectedStartAndEnd() {
        return this.hasSelectedStartAndEnd;
    }


}

