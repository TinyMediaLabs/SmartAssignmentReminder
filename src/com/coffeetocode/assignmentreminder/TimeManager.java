package com.coffeetocode.assignmentreminder;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Robertas on 2015-05-11.
 */
public class TimeManager {

    // TODO: implement a setting for this.
    private static final int workTimeLimits[] = new int[]{180, 180, 180, 180, 180, 360, 360};
    // TODO: implement a setting for this.
    private static final int workTimes[] = new int[]{0, 15, 30, 60, 180, 360};
    // how much time a day, MAX, can be used for work, in minutes
    DBHandler dbHandler;
    List<Assignment> assignments;
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
        // get today's date
        Calendar today = Calendar.getInstance();

        // get all assignments
        assignments = dbHandler.getAllAssignments();

        // sort by deadline, ascending order?
        // might help to switch to descending order for easier iterations
        int a = 0;
        for (int i = 0; i < assignments.size() - 1; i++) {
            a = i;
            for (int j = i + 1; j < assignments.size(); j++) {
                if (assignments.get(a).getDeadline().compareTo(assignments.get(j).getDeadline()) == 1) {
                    a = j;
                }
            }
            if (a != i) {
                // reworked swap
                Assignment temp = assignments.get(a);
                assignments.set(a, assignments.get(i));
                assignments.set(i, temp);
            }
        }



    }

    private class Task {
        public Calendar date;

    }

}
