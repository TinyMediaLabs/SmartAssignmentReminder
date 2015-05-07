package com.coffeetocode.assignmentreminder;

public class Card {
    private int ID;
	private String title;
	private String description;
    private String subject;

    public Card(int ID, String title, String description, String subject) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.subject = subject;
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
}
