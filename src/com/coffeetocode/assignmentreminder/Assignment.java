package com.coffeetocode.assignmentreminder;

import java.util.Calendar;

public class Assignment {
    private int ID;
	private String title;
	private String description;
    private Calendar deadline;
    private String subject;
    private int difficulty;
    private Calendar reminder;

    public Assignment(String title, String description, Calendar deadline, String subject, int difficulty, Calendar reminder) {
        this.title = title;
		this.description = description;
        this.deadline = deadline;
        this.subject = subject;
        this.difficulty = difficulty;
        this.reminder = reminder;
    }

    public Assignment(int id, String title, String description, Calendar deadline, String subject, int difficulty, Calendar reminder)
    {
        this.ID = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.subject = subject;
        this.difficulty = difficulty;
        this.reminder = reminder;
    }

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Calendar getReminder() {
        return reminder;
    }

    public void setReminder(Calendar reminder) {
        this.reminder = reminder;
    }

    public String getCalendarString(Calendar calendar) {
        return new String(calendar.get(Calendar.YEAR) + "-" +
                calendar.get(Calendar.MONTH) +
                "-" + calendar.get(Calendar.DAY_OF_MONTH) +
                "-" + calendar.get(Calendar.HOUR_OF_DAY) + "-" +
                calendar.get(Calendar.MINUTE) + "-");
    }
}
