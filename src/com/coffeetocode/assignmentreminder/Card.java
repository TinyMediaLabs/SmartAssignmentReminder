package com.coffeetocode.assignmentreminder;

import java.util.Calendar;

public class Card {
    private int ID;
    private String title;
    private String description;
    private String subject;
    private Calendar deadline;
    private String difficulty;

    public Card(int ID, String title, String description, String subject, Calendar deadline, String difficulty) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.deadline = deadline;
        this.difficulty = difficulty;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
