package com.example.chenduanjin.timerpicker;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by chenduanjin on 10/10/14.
 */
public class TimePickerDialog extends DialogFragment implements TimePickerLayout.TimeChangeListener{

    final static String HOUR = "hour";
    final static String MINUTE = "minute";
    public static TimePickerDialog newInstance(int hour, int minute) {
        TimePickerDialog tp = new TimePickerDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(HOUR, hour);
        bundle.putInt(MINUTE, minute);
        tp.setArguments(bundle);
        return tp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = this.getArguments();
        TimePickerLayout tp = new TimePickerLayout(this.getActivity().getApplicationContext());
        tp.setGravity(Gravity.CENTER);
        tp.setHourAndMinutes(bundle.getInt(HOUR, 12), bundle.getInt(MINUTE, 0));
        return tp;
    }
    @Override
    public void onTimeChanged(int hour, int minute) {

    }
}
