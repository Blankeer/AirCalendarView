package com.blanke.aircalendarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.blanke.lib.view.AirCalendarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AirCalendarView calendarView = (AirCalendarView) findViewById(R.id.calendarView);
        calendarView.setOnSelectedDayListener(new AirCalendarView.OnSelectedDayListener() {
            @Override
            public void onDaySelected(Calendar startDay, Calendar endDay) {
                Log.e("start:", startDay + ",end:" + endDay) ;
            }
        });
    }
}
