package com.coffeetocode.assignmentreminder;

import java.util.Calendar;

/**
 * Created by Samsung on 5/23/2015.
 */
public class Day {
    private int ID;
    private Calendar date;
    private String assignments;
    private int busy;

    public Day(int ID, Calendar date, String assignments, int busy) {
        this.ID = ID;
        this.date = date;
        this.assignments = assignments;
        this.busy = busy;
    }

    public Day(Calendar date, String assignments, int busy) {
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

    public String getAssignments() {
        return assignments;
    }

    public void setAssignments(String assignments) {
        this.assignments = assignments;
    }

    public int getBusy() {
        return busy;
    }

    public void setBusy(int busy) {
        this.busy = busy;
    }

    public void addBusy(int busy) {
        this.busy += busy;
    }

    public void addAssignment(int id) {
        this.assignments += id + ",";
    }

    public int[] getAssignmentsArray() {
        String[] assignmentString = this.assignments.split(",");
        int[] assignments = new int[10];
        for (int i = 0; i < assignmentString.length; i++) {
            assignments[i] = Integer.getInteger(assignmentString[i]);
        }
        return assignments;
    }

    public void setAssignmentsInString(int[] assignments) {
        String assignmentString = "";
        for (int i = 0; i < assignments.length; i++) {
            assignmentString += assignments[i] + ",";
        }
        this.assignments = assignmentString;
    }

    public String getDateString() {
        return new String(this.date.get(Calendar.YEAR) + "-" +
                this.date.get(Calendar.MONTH) + "-" +
                this.date.get(Calendar.DAY_OF_MONTH) + "-" +
                this.date.get(Calendar.HOUR_OF_DAY) + "-" +
                this.date.get(Calendar.MINUTE) + "-");
    }
}
