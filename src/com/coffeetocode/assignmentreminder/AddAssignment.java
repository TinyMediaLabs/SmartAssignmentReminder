package com.coffeetocode.assignmentreminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import java.util.Locale;

/**
 * Created by Samsung on 4/17/2015.
 */
public class AddAssignment extends FragmentActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Button timeDisplay;
    Button dateDisplay;
    EditText Description;
    EditText Title;
    EditText Subject;
    Calendar c = Calendar.getInstance();
    DBHandler dbHandler = new DBHandler(this);

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setTime(c);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTime(c);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setTime(Calendar calendar) {
        this.c = calendar;
    }

    void updateTimeDateDisplay() {
        dateDisplay.setText(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) +
                ", " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                " " + c.get(Calendar.DATE) +
                ", " + c.get(Calendar.YEAR));
        timeDisplay.setText(String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_assignment);

        timeDisplay = new Button(this);
        timeDisplay = (Button) findViewById(R.id.timeDisplay);
        dateDisplay = new Button(this);
        dateDisplay = (Button) findViewById(R.id.dateDisplay);
        Description = (EditText) findViewById(R.id.editText5);
        Title = (EditText) findViewById(R.id.editText);
        Subject = (EditText) findViewById(R.id.editText2);

        updateTimeDateDisplay();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        updateTimeDateDisplay();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, day);
        updateTimeDateDisplay();
    }

    public void addAssignment() {
        dbHandler.addAssignment(new Assignment(Title.getText().toString(),
                Description.getText().toString(),
                (double) c.getTimeInMillis() / 1000,
                Subject.getText().toString()));
        Toast.makeText(this, "Assignment saved", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_accept:
                //Something is done here
                addAssignment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_assignment, menu);
        MenuItem options = menu.findItem(R.id.action_settings);
        options.setVisible(false);
        return true;
    }
}
