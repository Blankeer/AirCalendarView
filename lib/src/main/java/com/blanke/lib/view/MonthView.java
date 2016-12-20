package com.blanke.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blanke.lib.R;
import com.blanke.lib.view.bean.CalendarDayModel;
import com.blanke.lib.view.bean.CalendarMonthModel;

import java.util.List;


public class MonthView extends View {

    public interface OnDayClickListener {
        void onDayClick(MonthView monthView, CalendarDayModel day);
    }

    private int selectedCircleColor;
    private int unavailableDayTextColor;
    private int dayTextColor;
    private int selectedDayTextColor;
    private int monthTextColor;
    private float dayTextSize;
    private float monthTextSize;
    private Paint selectedCirclePaint;
    private Paint unavailableDayPaint;
    private Paint dayPaint;
    private Paint selectedDayPaint;
    private Paint monthPaint;
    private Paint todayCirclePaint;//今日下面的小圆点
    private int todayCircleRadius;
    private CalendarMonthModel mCalendarMonthModel;
    private float itemHeight;
    private float itemWidth;
    private float monthHeight;
    private float offsetRowHeight = 15;
    private OnDayClickListener dayClickListener;

    public MonthView(Context context) {
        super(context);
        init(null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MonthView);
        selectedCircleColor = typedArray.getColor(R.styleable.MonthView_selectedCircleColor, Color.BLACK);
        selectedDayTextColor = typedArray.getColor(R.styleable.MonthView_selectedDayTextColor, Color.WHITE);
        dayTextColor = typedArray.getColor(R.styleable.MonthView_dayTextColor, Color.BLACK);
        monthTextColor = typedArray.getColor(R.styleable.MonthView_monthTextColor, Color.BLACK);
        unavailableDayTextColor = typedArray.getColor(R.styleable.MonthView_unavailableDayTextColor, Color.GRAY);
        dayTextSize = typedArray.getDimension(R.styleable.MonthView_dayTextSize, 40);
        monthTextSize = typedArray.getDimension(R.styleable.MonthView_monthTextSize, 40);
        typedArray.recycle();
        selectedCirclePaint = new Paint();
        selectedCirclePaint.setAntiAlias(true);
        selectedCirclePaint.setColor(selectedCircleColor);
        selectedCirclePaint.setTextAlign(Paint.Align.CENTER);
        selectedCirclePaint.setStyle(Paint.Style.FILL);
        dayPaint = new Paint(selectedCirclePaint);
        dayPaint.setColor(dayTextColor);
        dayPaint.setTextSize(dayTextSize);
        unavailableDayPaint = new Paint(dayPaint);
        unavailableDayPaint.setColor(unavailableDayTextColor);
        selectedDayPaint = new Paint(dayPaint);
        selectedDayPaint.setColor(selectedDayTextColor);
        monthPaint = new Paint(dayPaint);
        monthPaint.setColor(monthTextColor);
        monthPaint.setTextSize(monthTextSize);
        todayCirclePaint = new Paint(dayPaint);
        todayCircleRadius = 4;//今日小圆点
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (itemHeight == 0) {
            itemWidth = itemHeight = width / 7F;
            monthHeight = itemHeight;
        }
        int height = (int) (width - itemHeight + offsetRowHeight * 3);
        if (mCalendarMonthModel != null) {
            int itemCount = mCalendarMonthModel.getNumberDaysInMonth()
                    + mCalendarMonthModel.getDayOffset();
            float temp = itemCount / 7F;
            if (temp > 5) {
                height += itemHeight;
            }
        } else {
            height = 0;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        drawMonthTitle(canvas);
        drawMonthDay(canvas);
    }

    private void drawMonthTitle(Canvas canvas) {
        if (mCalendarMonthModel == null) {
            return;
        }
        String monthText = mCalendarMonthModel.getMonthText();
        Rect textBounds = new Rect();
        monthPaint.getTextBounds(monthText, 0, monthText.length(), textBounds);
        canvas.drawText(monthText, itemWidth + textBounds.exactCenterX() / 2,
                monthHeight/2 - textBounds.exactCenterY() / 2, monthPaint);
    }

    private void drawMonthDay(Canvas canvas) {
        if (mCalendarMonthModel == null) {
            return;
        }
        List<CalendarDayModel> days = mCalendarMonthModel.getDays();
        float h = monthHeight + offsetRowHeight;
        int dayOffset = mCalendarMonthModel.getDayOffset();
        for (int i = 0; i < days.size(); i++) {
            CalendarDayModel day = days.get(i);
            //day item 坐标
            float w = itemWidth / 2 * (dayOffset * 2 + 1);
            //绘制day
            String dayText = day.day + "";
            Rect textBounds = new Rect();
            Paint dPaint = dayPaint;
            //绘制是否选中
            float left = w - itemWidth / 2;
            float top = h - itemHeight / 2;
            float bottom = h + itemHeight / 2;
            float right = w + itemWidth / 2;
            if (day.isUnavailable) {//不可选中
                dPaint = unavailableDayPaint;
            } else if (day.isSelected()) {
                dPaint = selectedDayPaint;
                if (mCalendarMonthModel.hasSelectedStartAndEnd()) {
                    if (i == 0 && !day.isSelectedStartDay) {//每个月第一天
                        canvas.drawRect(0, top, right - itemWidth, bottom, selectedCirclePaint);
                    } else if (i == days.size() - 1 && !day.isSelectedEndDay) {//最后一天
                        canvas.drawRect(left + itemWidth, top,
                                7 * itemWidth, bottom, selectedCirclePaint);
                    }
                }
                if (day.isBetweenStartAndEndSelected) {
                    canvas.drawRect(left, top, right, bottom, selectedCirclePaint);
                } else if (day.isSelectedStartDay) {
                    canvas.drawCircle((left + right) / 2,
                            (top + bottom) / 2, itemHeight / 2, selectedCirclePaint);
                    if (mCalendarMonthModel.hasSelectedStartAndEnd()) {
                        canvas.drawRect((left + right) / 2, top, right, bottom, selectedCirclePaint);
                    }
                } else {//select end
                    canvas.drawCircle((left + right) / 2,
                            (top + bottom) / 2, itemHeight / 2, selectedCirclePaint);
                    if (mCalendarMonthModel.hasSelectedStartAndEnd()) {
                        canvas.drawRect(left, top, (left + right) / 2, bottom, selectedCirclePaint);
                    }
                }
            }
            //绘制文本day
            dPaint.getTextBounds(dayText, 0, dayText.length(), textBounds);
            canvas.drawText(dayText, w, h - textBounds.exactCenterY(), dPaint);
            //绘制是否是今日
            if (day.isToday) {
                if (day.isSelected()) {
                    todayCirclePaint.setColor(this.selectedDayPaint.getColor());
                } else {
                    todayCirclePaint.setColor(this.dayPaint.getColor());
                }
                canvas.drawCircle(w, h + itemWidth / 4 + this.todayCircleRadius * 2,
                        ((float) this.todayCircleRadius), this.todayCirclePaint);
            }
            dayOffset++;
            if (dayOffset == 7) {
                dayOffset = 0;
                h += itemHeight + offsetRowHeight;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            int position = (int) ((y - monthHeight - offsetRowHeight / 2 + itemHeight / 2)
                    / (itemHeight + offsetRowHeight)) * 7;
            position += (int) (x / itemWidth) + 1;
            position -= mCalendarMonthModel.getDayOffset();
            CalendarDayModel day = mCalendarMonthModel.getDayModel(position);
            if (day != null && dayClickListener != null) {
                dayClickListener.onDayClick(this, day);
            }
        }
        return true;
    }

    public CalendarMonthModel getCalendarMonthModel() {
        return mCalendarMonthModel;
    }

    public void setCalendarMonthModel(CalendarMonthModel mCalendarMonthModel) {
        this.mCalendarMonthModel = mCalendarMonthModel;
        invalidate();
    }

    public OnDayClickListener getDayClickListener() {
        return dayClickListener;
    }

    public void setDayClickListener(OnDayClickListener dayClickListener) {
        this.dayClickListener = dayClickListener;
    }
}