package com.coffeetocode.assignmentreminder;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Robertas on 2015-04-18.
 */
public class TimePickerFragment extends DialogFragment {

    private Activity activity;
    private Calendar c = Calendar.getInstance();
    private TimePickerDialog.OnTimeSetListener listener;

    public void setTime(Calendar calendar)
    {
        this.c = calendar;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

        // This error will remind you to implement an OnTimeSetListener
        //   in your Activity if you forget
        try {
            listener = (TimePickerDialog.OnTimeSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTimeSetListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog dialog = new TimePickerDialog(activity, listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        dialog.setTitle("Set time");
        return dialog;
    }
}