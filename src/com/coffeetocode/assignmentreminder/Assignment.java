package com.coffeetocode.assignmentreminder;

import java.lang.String;

public class Assignment {
    private int ID;
	private String title;
	private String description;
	private double dueDate; // UNIX time
	private String subject;

	public Assignment (String title, String description, double dueDate, String subject)
	{
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.subject = subject;
	}

    public Assignment (int id, String title, String description, double dueDate, String subject)
    {
        this.ID = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.subject = subject;
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

    public double getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(double dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

}
