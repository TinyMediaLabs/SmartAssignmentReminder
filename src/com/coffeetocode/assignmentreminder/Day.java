package com.coffeetocode.assignmentreminder;

import java.util.Calendar;

/**
 * Created by Samsung on 5/23/2015.
 */
public class Day {
    private int ID;
    private Calendar date;
    private int[] assignments;
    private int busy;

    public Day(int ID, Calendar date, int[] assignments, int busy) {
        this.ID = ID;
        this.date = date;
        this.assignments = assignments;
        this.busy = busy;
    }

    public Day(Calendar date, int[] assignments, int busy) {
        this.date = date;
        this.assignments = assignments;
        this.busy = busy;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int[] getAssignments() {
        return assignments;
    }

    public void setAssignments(int[] assignments) {
        this.assignments = assignments;
    }

    public int getBusy() {
        return busy;
    }

    public void setBusy(int busy) {
        this.busy = busy;
    }

    public String getAssignmentsString() {
        String assignments = "";
        for (int i = 0; i < this.assignments.length; i++) {
            assignments += this.assignments[i] + ",";
        }
        return assignments;
    }

    public String getDateString() {
        return new String(this.date.get(Calendar.YEAR) + "-" +
                this.date.get(Calendar.MONTH) + "-" +
                this.date.get(Calendar.DAY_OF_MONTH) + "-" +
                this.date.get(Calendar.HOUR_OF_DAY) + "-" +
                this.date.get(Calendar.MINUTE) + "-");
    }
}
