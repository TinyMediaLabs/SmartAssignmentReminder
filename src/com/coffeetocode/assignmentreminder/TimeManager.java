package com.coffeetocode.assignmentreminder;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Robertas on 2015-05-11.
 */
public class TimeManager {

    // TODO: implement a setting for this.
    private static final int dayWorkLimit = 180;
    // TODO: implement a setting for this.
    private static final int workTimes[] = new int[]{0, 15, 30, 60, 360, 1000};
    // how much time a day, MAX, can be used for work, in minutes
    DBHandler dbHandler;
    // worktimes for corresponding task difficulties 0-5
    private Context context;

    public TimeManager(Context context) {
        this.context = context;
        dbHandler = new DBHandler(this.context);
    }

    public String getDisplayTime(int minutes) {
        int hr = minutes / 60;
        int min = minutes % 60;

        String result = String.format("%02d:%02d", hr, min);

        return result;
    }

    public String getDisplayTime(Calendar c) {
        int hr = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        String result = String.format("%02d:%02d", hr, min);

        return result;
    }

    public void calculateReminders() {
        Calendar today = Calendar.getInstance();

        List<Assignment> assignments = dbHandler.getAllAssignments();

        for (int i = assignments.size() - 1; i >= 0; i--) {

        }


    }

}
