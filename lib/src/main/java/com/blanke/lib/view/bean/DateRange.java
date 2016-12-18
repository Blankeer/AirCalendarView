package com.blanke.lib.view.bean;


import com.blanke.lib.view.utils.CalendarUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by blanke on 16-12-14.
 */

public class DateRange {
    protected Calendar startDate;
    protected Calendar endDate;
    protected Calendar toDay;//今日
    protected boolean isSelectFuture = true;//只能选择未来日期
    protected int daySelectOffset = 1;//选择时候日期偏移

    public DateRange(boolean isSelectFuture, int daySelectOffset) {
        this.isSelectFuture = isSelectFuture;
        this.daySelectOffset = daySelectOffset;
        toDay = CalendarUtils.getToDay();
    }

    protected boolean isSelectEnable(Calendar day) {
        if (isSelectFuture) {
            if (day.before(toDay)) {
                return false;
            }
        } else {
            if (day.after(toDay)) {
                return false;
            }
        }
        return true;
    }

    //返回true代表需要刷新视图
    public boolean selectDay(Calendar day) {
        if (day == null) {
            startDate = endDate = null;
            return true;
        }
        if (!isSelectEnable(day)) {
            return false;
        }
        if (startDate == null) {
            startDate = day;
        } else if (endDate == null) {
            if (day.after(startDate)) {
                endDate = day;
            } else if (day.equals(startDate)) {//重复点击
                startDate = null;
            } else {
                endDate = startDate;
                startDate = day;
            }
        } else {
            if (startDate.equals(day)) {
                startDate = endDate;
                endDate = null;
            } else if (endDate.equals(day)) {
                endDate = null;
            } else {
                Calendar startDateAfter = (Calendar) startDate.clone();
                startDateAfter.add(Calendar.DAY_OF_MONTH, daySelectOffset);
                Calendar endDateAfter = (Calendar) endDate.clone();
                endDateAfter.add(Calendar.DAY_OF_MONTH, daySelectOffset);
                Calendar dayDateAfter = (Calendar) day.clone();
                dayDateAfter.add(Calendar.DAY_OF_MONTH, daySelectOffset);
                if (!dayDateAfter.before(startDate)
                        && !day.after(startDateAfter)) {//start+1>=x>=start-1
                    startDate = day;
                } else if (!dayDateAfter.before(endDate)
                        && !day.after(endDateAfter)) {//end+1>=x>=end-1
                    endDate = day;
                } else {//不在start/end附近的offset内
                    startDate = day;
                    endDate = null;
                }
            }
        }
        return true;
    }

    private boolean hasSelectedStartAndEnd() {
        return startDate != null && endDate != null;
    }

    public boolean clickDay(Calendar day, List<CalendarMonthModel> list) {
        if (selectDay(day)) {
            for (CalendarMonthModel calendarMonthModel : list) {
                changeMonth(calendarMonthModel);
            }
            return true;
        }
        return false;
    }

    public void changeMonth(CalendarMonthModel monthModel) {
        monthModel.setHasSelectedStartAndEnd(hasSelectedStartAndEnd());
        boolean unSelcted = false;
        if (startDate == null) {
            unSelcted = true;
        }
        for (CalendarDayModel calendarDayModel : monthModel.getDays()) {
            Calendar d = calendarDayModel.dayCalendar;
            if (isSelectFuture) {
                calendarDayModel.isUnavailable = d.before(toDay);
            } else {
                calendarDayModel.isUnavailable = d.after(toDay);
            }
            if (calendarDayModel.isUnavailable) {
                continue;
            }
            calendarDayModel.unSelected();//重置选中状态
            if (unSelcted) {
                continue;
            }
            if (endDate == null) {
                if (startDate.equals(d)) {
                    calendarDayModel.isSelectedStartDay = true;
                }
            } else {
                if (d.before(startDate)) {
                    calendarDayModel.unSelected();
                } else if (d.equals(startDate)) {
                    calendarDayModel.isSelectedStartDay = true;
                } else if (d.before(endDate)) {
                    calendarDayModel.isBetweenStartAndEndSelected = true;
                } else if (d.equals(endDate)) {
                    calendarDayModel.isSelectedEndDay = true;
                } else {
                    calendarDayModel.unSelected();
                }
            }
        }
    }

    public boolean isSelectFuture() {
        return isSelectFuture;
    }

    public void setSelectFuture(boolean selectFuture) {
        isSelectFuture = selectFuture;
    }

    public int getDaySelectOffset() {
        return daySelectOffset;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Calendar getToDay() {
        return toDay;
    }

    public void setToDay(Calendar toDay) {
        this.toDay = toDay;
    }

    public void setDaySelectOffset(int daySelectOffset) {
        this.daySelectOffset = daySelectOffset;
    }
}
