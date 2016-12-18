package com.blanke.lib.view.bean;

import java.util.Calendar;

public class CalendarDayModel {
    public final int day;
    public boolean isInThePast;
    public boolean isToday;
    public boolean isInTheFuture;

    public boolean isBetweenStartAndEndSelected;
    public boolean isSelectedEndDay;
    public boolean isSelectedStartDay;

    public boolean isUnavailable;
    public Calendar dayCalendar;

    public CalendarDayModel(int day, Calendar dayCalendar) {
        this.day = day;
        this.dayCalendar = dayCalendar;
    }

    public void unSelected() {
        isBetweenStartAndEndSelected = isSelectedEndDay = isSelectedStartDay = false;
    }

    public boolean isSelected() {
        return (this.isSelectedStartDay) ||
                (this.isSelectedEndDay) ||
                (this.isBetweenStartAndEndSelected);
    }
}

