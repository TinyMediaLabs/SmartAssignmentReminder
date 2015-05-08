package com.coffeetocode.assignmentreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Samsung on 4/17/2015.
 */
public class EditAssignment extends ActionBarActivity {

    Button timeDisplay;
    Button dateDisplay;

    EditText Description;
    EditText Title;
    EditText Subject;

    Calendar c = Calendar.getInstance();
    DBHandler dbHandler = new DBHandler(this);
    Assignment assignment;

    SeekBar seekBar;
    int difficulty = 0;

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
        setContentView(R.layout.activity_edit_assignment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int assignmentID = intent.getIntExtra("assignmentID", 0);
        assignment = dbHandler.getAssignment(assignmentID);

        timeDisplay = new Button(this);
        timeDisplay = (Button) findViewById(R.id.timeDisplay);
        dateDisplay = new Button(this);
        dateDisplay = (Button) findViewById(R.id.dateDisplay);
        Description = (EditText) findViewById(R.id.editText5);
        Title = (EditText) findViewById(R.id.editText);
        Subject = (EditText) findViewById(R.id.editText2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        // TODO: set time and date of the pickers here, taken from assignment.getDeadline()

        Description.setText(assignment.getDescription());
        Title.setText(assignment.getTitle());
        Subject.setText(assignment.getSubject());



    }
}
