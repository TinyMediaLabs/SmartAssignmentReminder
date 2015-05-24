package com.coffeetocode.assignmentreminder;

import android.content.Context;
import android.widget.Toast;

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
    List<Day> days = dbHandler.getAllDays();
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
        days = dbHandler.getAllDays();

        // sort by deadline, ascending order?
        // might help to switch to descending order for easier iterations
        int a = 0;
        for (int i = 0; i < assignments.size() - 1; i++) {
            a = i;
            for (int j = i + 1; j < assignments.size(); j++) {
                if (assignments.get(a).getDeadline().compareTo(assignments.get(j).getDeadline()) == 1) {
                    a = j;
                } else if (assignments.get(a).getDeadline().compareTo(assignments.get(j).getDeadline()) == 0) {
                    if (assignments.get(a).getDifficulty() < assignments.get(j).getDifficulty()) {
                        swapAssignments(assignments, a, j);
                    }
                }
            }
            if (a != i) {
                // reworked swap
                swapAssignments(assignments, a, i);
            }
        }

        resetBusyTime();
        putAssignmentsToTable();
        checkTimeTable();
    }

    private void checkTimeTable() {
        for (int i = 0; i < days.size(); i++) {
            int[] assignmentArray = days.get(i).getAssignmentsArray();
            for (int j = 0; j < assignmentArray.length; j++) {
                Assignment temporaryAssignment = dbHandler.getAssignment(assignmentArray[j]);
                if (temporaryAssignment.getDeadline().compareTo(days.get(i).getDate()) != -1) {
                    Toast.makeText(this.context, "Too much work", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void putAssignmentsToTable() {
        int j = 0;
        for (int i = 0; i < assignments.size(); i++) {
            int time = workTimes[assignments.get(i).getDifficulty()];
            if (time == 0) {
                days.get(j).addAssignment(assignments.get(i).getID());
            } else {
                while (time != 0) {
                    time = workTimes[assignments.get(i).getDifficulty()];
                    if (time < workTimeLimits[days.get(j).getDate().DAY_OF_WEEK] - days.get(j).getBusy()) {
                        days.get(j).addBusy(time);
                        days.get(j).addAssignment(assignments.get(i).getID());
                        time = 0;
                    } else if (time > workTimeLimits[days.get(j).getDate().DAY_OF_WEEK] - days.get(j).getBusy()) {
                        time -= workTimeLimits[days.get(j).getDate().DAY_OF_WEEK] - days.get(j).getBusy();
                        days.get(j).setBusy(workTimeLimits[days.get(j).getDate().DAY_OF_WEEK]);
                    }
                    if (time != 0) {
                        j += 1;
                    }
                }
            }
        }
        for (int i = 0; i < days.size(); i++) {
            dbHandler.updateDay(days.get(i));
        }
    }

    private void resetBusyTime() {
        for (int i = 0; i < days.size(); i++) {
            days.get(i).setBusy(0);
        }
    }

    private void swapAssignments(List<Assignment> assignments, int a, int i) {
        Assignment temp = assignments.get(a);
        assignments.set(a, assignments.get(i));
        assignments.set(i, temp);
    }

    private class Task {
        public Calendar date;

    }

}
