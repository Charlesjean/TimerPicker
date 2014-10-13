package com.example.chenduanjin.timerpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.Button;

/**
 * Created by chenduanjin on 10/10/14.
 */
public class TimePickerLayout extends LinearLayout implements View.OnTouchListener{

    public interface TimeChangeListener {
        void onTimeChanged(int hour, int minute);
    }
    Button mHour;
    Button mMinute;
    Button PM_AM;
    Button mDoneBtn;
    ClockPanel mHourClock;
    ClockPanel mMinuteClock;
    String[] minutesStr = new String[12];
    String[] hoursStr = new String[12];

    int[] hours = {12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    int[] minutes= {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55};

    //these two variables are use to render clockpanel
    int defaultHour = 12;
    int defaultMinute = 0;
    TimeChangeListener mListener;

    //variables for touch event handle
    float mStartX;
    float mStartY;

    public TimePickerLayout(Context context) {
        super(context);
        init(context);
    }

    public TimePickerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflate = LayoutInflater.from(context);
        inflate.inflate(R.layout.timepicker_main, this, true);
        mHour = (Button)findViewById(R.id.timer_hour);
        mMinute = (Button)findViewById(R.id.timer_minute);
        PM_AM = (Button)findViewById(R.id.pm_btn);
        mHourClock = (ClockPanel)findViewById(R.id.hour_clock);
        mMinuteClock = (ClockPanel)findViewById(R.id.minute_clock);
        mDoneBtn = (Button)findViewById(R.id.timer_done);

        for (int i = 0; i < minutes.length; ++i) {
            minutesStr[i] = String.format("%02d", minutes[i]);
            hoursStr[i] = String.format("%2d", hours[i]);
        }

        mHourClock.initialize(hoursStr, defaultHour);
        mHourClock.setClockType(ClockPanel.ClockType.HOUR);
        mMinuteClock.initialize(minutesStr, defaultMinute);
        mMinuteClock.setClockType(ClockPanel.ClockType.MINUTE);

        mDoneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO we need to get user set time
                int hour = 12, minute = 0;
                mListener.onTimeChanged(hour, minute);
            }
        });
        this.setOnTouchListener(this);
    }

    /**
     * Use this method to set the default selected hour and minute
     * @param hour
     * @param minute
     */
    public void setHourAndMinutes(int hour, int minute) {
        if (hour > 12)
            hour -= 12;
        defaultHour = hour == 0 ? 12 : hour;
        defaultMinute = minute;
        mHourClock.setSelectedNumber(defaultHour);
        mMinuteClock.setSelectedNumber(defaultMinute);
        setHour(hour);
        setMinute(minute);
    }

    public void setHour(int hour) {
        mHour.setText("" + hour);
    }

    public void setMinute(int minute) {
        mMinute.setText("" + String.format("%02d",minute));
    }

    public void setTimeChangeListener(TimeChangeListener listener) {
        this.mListener = listener;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final float eventX = event.getX();
        final float eventY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int am_pm = mHourClock.touchesInAMOrPM(eventX, eventY);

        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {

        }
        return false;
    }
}


