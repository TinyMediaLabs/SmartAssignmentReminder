package com.coffeetocode.assignmentreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PM2063F6 on 2014-12-13.
 */
public class DBHandler extends SQLiteOpenHelper
{
    //Database definition
    private static final int VERSION = 1;
    private static final String DB_NAME = "assignment_db";

    //Assignments table definition
    private static final String ASSIGNMENTS_TABLE       = "assignments";
    private static final String ASSIGNMENT_ID           = "ID";
    private static final String ASSIGNMENT_TITLE        = "title";
    private static final String ASSIGNMENT_DESCRIPTION  = "description";
    private static final String DUEDATE = "dueDate";
    private static final String SUBJECT                 = "subject";

    //Cards table definition
    private static final String CARDS_TABLE = "cards";
    private static final String CARD_ID = "ID";
    private static final String CARD_TITLE = "title";
    private static final String CARD_DESCRIPTION = "description";
    private static final String CARD_SUBJECT = "subject";

    public DBHandler(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_ASSIGNMENTS_TABLE = "" +
                "CREATE TABLE " + ASSIGNMENTS_TABLE
                + " ("
                + ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASSIGNMENT_TITLE + " TEXT,"
                + ASSIGNMENT_DESCRIPTION + " TEXT,"
                + DEADLINE + " TEXT,"
                + SUBJECT + " TEXT,"
                + DIFFICULTY + " TEXT,"
                + REMINDER + " TEXT"
                + ")";

        db.execSQL(CREATE_ASSIGNMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENTS_TABLE);
        onCreate(sqLiteDatabase);
    }

    void addAssignment(Assignment assignment)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSIGNMENT_TITLE,       assignment.getTitle());
        values.put(ASSIGNMENT_DESCRIPTION, assignment.getDescription());
        values.put(DEADLINE, assignment.getCalendarString(assignment.getDeadline()));
        values.put(SUBJECT,                assignment.getSubject());
        values.put(DIFFICULTY, assignment.getDifficulty());
        values.put(REMINDER, assignment.getCalendarString(assignment.getReminder()));

        db.insert(ASSIGNMENTS_TABLE, null, values);
    }

    void deleteAssignment(int ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ASSIGNMENTS_TABLE, ASSIGNMENT_ID + " = " + String.valueOf(ID), null);
    }

    public Assignment getAssignment(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                ASSIGNMENTS_TABLE,
                new String[]{ASSIGNMENT_ID, ASSIGNMENT_TITLE, ASSIGNMENT_DESCRIPTION, DEADLINE, SUBJECT, DIFFICULTY, REMINDER},
                ASSIGNMENT_ID + " = " + String.valueOf(id),
                null, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Assignment assignment= new Assignment(
                cursor.getInt(0),                           //ASSIGNMENT_ID
                cursor.getString(1),                        //ASSIGNMENT_TITLE
                cursor.getString(2),                        //ASSIGNMENT_DESCRIPTION
                getCalendarCalendar(cursor.getString(3)),   //DEADLINE
                cursor.getString(4),                        //SUBJECT
                cursor.getString(5),                        //DIFFICULTY
                getCalendarCalendar(cursor.getString(6))    //REMINDER
        );
        return assignment;

    }

    public List<Assignment> getAllAssignments()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Assignment> assignmentList = new ArrayList<Assignment>();

        String selectQuery = "SELECT * FROM " + ASSIGNMENTS_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                Assignment assignment= new Assignment(
                        cursor.getInt(0),                           //ASSIGNMENT_ID
                        cursor.getString(1),                        //ASSIGNMENT_TITLE
                        cursor.getString(2),                        //ASSIGNMENT_DESCRIPTION
                        getCalendarCalendar(cursor.getString(3)),   //DEADLINE
                        cursor.getString(4),                        //SUBJECT
                        cursor.getString(5),                        //DIFFICULTY
                        getCalendarCalendar(cursor.getString(6))    //REMINDER
                );

                assignmentList.add(assignment);
            }
            while (cursor.moveToNext());
        }

        return assignmentList;

    }

    public int updateAssignment(Assignment assignment)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSIGNMENT_TITLE,       assignment.getTitle());
        values.put(ASSIGNMENT_DESCRIPTION, assignment.getDescription());
        values.put(DEADLINE, assignment.getCalendarString(assignment.getDeadline()));
        values.put(SUBJECT,                assignment.getSubject());
        values.put(DIFFICULTY, assignment.getDifficulty());
        values.put(REMINDER, assignment.getCalendarString(assignment.getReminder()));

        return db.update(ASSIGNMENTS_TABLE, values, ASSIGNMENT_ID + " = " + String.valueOf(assignment.getID()), null);
    }

    public int getAssignmentCount()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String countQuery = "SELECT * FROM " + ASSIGNMENTS_TABLE;

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

    public Calendar getCalendarCalendar(String string) {
        String delims = "[-]+";
        String[] reminderString = string.split(delims);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(reminderString[0]));
        c.set(Calendar.MONTH, Integer.parseInt(reminderString[1]));
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(reminderString[2]));
        c.set(Calendar.HOUR, Integer.parseInt(reminderString[3]));
        c.set(Calendar.MINUTE, Integer.parseInt(reminderString[4]));
        return c;
    }
}
