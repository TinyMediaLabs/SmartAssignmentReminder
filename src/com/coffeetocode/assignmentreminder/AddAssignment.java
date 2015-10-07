package com.coffeetocode.assignmentreminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Samsung on 4/17/2015.
 */
public class AddAssignment extends ActionBarActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Button timeDisplay;
    Button dateDisplay;

    EditText Description;
    EditText Title;
    EditText Subject;

    Calendar c = Calendar.getInstance();
    Calendar reminder = Calendar.getInstance();
    DBHandler dbHandler = new DBHandler(this);

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
        setContentView(R.layout.activity_assignment_data);
        timeDisplay = new Button(this);
        timeDisplay = (Button) findViewById(R.id.timeDisplay);
        dateDisplay = new Button(this);
        dateDisplay = (Button) findViewById(R.id.dateDisplay);
        Description = (EditText) findViewById(R.id.editText5);
        Title = (EditText) findViewById(R.id.editText);
        Subject = (EditText) findViewById(R.id.editText2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

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

        reminder.setTimeInMillis(0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

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
        if (Title.getText().toString().isEmpty() && Description.getText().toString().isEmpty() &&
                Subject.getText().toString().isEmpty()) {
            Toast.makeText(this, "Nothing is filled", Toast.LENGTH_LONG).show();
        } else {
            dbHandler.addAssignment(new Assignment(Title.getText().toString(),
                    Description.getText().toString(),
                    c,
                    Subject.getText().toString(),
                    difficulty,
                    c));
            List<Day> dayList = dbHandler.getAllDays();
            if (dayList.get(dayList.size() - 1).getDate().compareTo(c) == -1) {
                Calendar temporaryCalendar = dayList.get(dayList.size() - 1).getDate();
                while (temporaryCalendar.compareTo(c) == -1) {
                    temporaryCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    dbHandler.createDay(new Day(temporaryCalendar, null, 0));
                }
            }
            //TODO:put all the assignments into days, check if there is enough time to do them
            Toast.makeText(this, "Assignment saved", Toast.LENGTH_SHORT).show();

            this.finish();
        }
    }

    public void save(View view) {
        addAssignment();
    }


    @Override
    public void onBackPressed() {
        if (Title.getText().toString().isEmpty() && Description.getText().toString().isEmpty() &&
                Subject.getText().toString().isEmpty()) {
            AddAssignment.super.onBackPressed();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Save assignment?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            addAssignment();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            AddAssignment.super.onBackPressed();
                        }
                    }).create().show();
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
