package com.coffeetocode.assignmentreminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Samsung on 4/17/2015.
 */
public class EditAssignment extends ActionBarActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    Button timeDisplay;
    Button dateDisplay;

    EditText Description;
    EditText Title;
    EditText Subject;

    Calendar c = Calendar.getInstance();
    Calendar reminder = Calendar.getInstance();

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

        difficulty = Integer.valueOf(assignment.getDifficulty());
        Description.setText(assignment.getDescription());
        Title.setText(assignment.getTitle());
        Subject.setText(assignment.getSubject());

        seekBar.setProgress(difficulty);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                difficulty = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        c = assignment.getDeadline();

        updateTimeDateDisplay();

    }

    @Override
    public void onBackPressed() {
        if (Title.getText().toString().equals(assignment.getTitle()) && Description.getText().toString().equals(assignment.getDescription()) &&
                Subject.getText().toString().equals(assignment.getSubject()) && difficulty == Integer.parseInt(assignment.getDifficulty().toString())) {
            EditAssignment.super.onBackPressed();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Save assignment?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            updateAssignment();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            EditAssignment.super.onBackPressed();
                        }
                    }).create().show();
        }
    }

    public void updateAssignment() {
        if (Title.getText().toString().equals(assignment.getTitle()) && Description.getText().toString().equals(assignment.getDescription()) &&
                Subject.getText().toString().equals(assignment.getSubject()) && difficulty == Integer.parseInt(assignment.getDifficulty().toString())) {
            Toast.makeText(this, "No changes have been made", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            dbHandler.updateAssignment(new Assignment(assignment.getID(),
                    Title.getText().toString(),
                    Description.getText().toString(),
                    c,
                    Subject.getText().toString(),
                    String.valueOf(difficulty),
                    reminder));
            Toast.makeText(this, "Assignment updated", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    public void save(View view) {
        updateAssignment();
    }
}
