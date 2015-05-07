package com.coffeetocode.assignmentreminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Robertas on 2015-04-18.
 */
public class DatePickerFragment extends DialogFragment {

    private Activity activity;
    private Calendar c = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener listener;

    public void setTime(Calendar calendar)
    {
        this.c = calendar;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

        try {
            listener = (DatePickerDialog.OnDateSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDateSetListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(activity, listener, year, month, day);
        dialog.setTitle("Set date");
        return dialog;
    }
}
