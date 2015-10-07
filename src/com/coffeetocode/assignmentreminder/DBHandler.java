package com.coffeetocode.assignmentreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
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
    private static final String DEADLINE = "deadline";
    private static final String SUBJECT                 = "subject";
    private static final String DIFFICULTY = "difficulty";
    private static final String REMINDER = "reminder";

    //Day table definition
    private static final String DAY_TABLE = "day";
    private static final String DAY_ID = "ID";
    private static final String DAY_DATE = "date";
    private static final String DAY_ASSIGNMENTS = "assignments";
    private static final String DAY_BUSY = "busy";

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
                + DIFFICULTY + " INTEGER,"
                + REMINDER + " TEXT"
                + ")";
        String CREATE_DAY_TABLE = "" +
                "CREATE TABLE " + DAY_TABLE
                + " ("
                + DAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DAY_DATE + " TEXT,"
                + DAY_ASSIGNMENTS + " TEXT,"
                + DAY_BUSY + " INTEGER"
                + ")";

        db.execSQL(CREATE_ASSIGNMENTS_TABLE);
        db.execSQL(CREATE_DAY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENTS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DAY_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void deleteAllAssignments() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(ASSIGNMENTS_TABLE, null, null);
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
        db.close();
    }

    void deleteAssignment(int ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ASSIGNMENTS_TABLE, ASSIGNMENT_ID + " = " + String.valueOf(ID), null);
        db.close();
    }

    public Assignment getAssignment(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                ASSIGNMENTS_TABLE,
                new String[]{ASSIGNMENT_ID, ASSIGNMENT_TITLE, ASSIGNMENT_DESCRIPTION, DEADLINE, SUBJECT, DIFFICULTY, REMINDER},
                ASSIGNMENT_ID + " = " + String.valueOf(id),
                null, null, null, null, null);
        if (cursor.getCount() == 0)
            return null;
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Assignment assignment= new Assignment(
                cursor.getInt(0),                           //ASSIGNMENT_ID
                cursor.getString(1),                        //ASSIGNMENT_TITLE
                cursor.getString(2),                        //ASSIGNMENT_DESCRIPTION
                getCalendarObject(cursor.getString(3)),   //DEADLINE
                cursor.getString(4),                        //SUBJECT
                cursor.getInt(5),                        //DIFFICULTY
                getCalendarObject(cursor.getString(6))    //REMINDER
        );
        db.close();
        return assignment;
    }

    public List<Assignment> getAllAssignments()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Assignment> assignmentList = new ArrayList<Assignment>();

        String selectQuery = "SELECT * FROM " + ASSIGNMENTS_TABLE;

        Cursor cursor;
        cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                Assignment assignment= new Assignment(
                        cursor.getInt(0),                           //ASSIGNMENT_ID
                        cursor.getString(1),                        //ASSIGNMENT_TITLE
                        cursor.getString(2),                        //ASSIGNMENT_DESCRIPTION
                        getCalendarObject(cursor.getString(3)),   //DEADLINE
                        cursor.getString(4),                        //SUBJECT
                        cursor.getInt(5),                        //DIFFICULTY
                        getCalendarObject(cursor.getString(6))    //REMINDER
                );

                assignmentList.add(assignment);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return assignmentList;
    }

    public int updateAssignment(Assignment assignment)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSIGNMENT_TITLE,       assignment.getTitle());
        values.put(ASSIGNMENT_DESCRIPTION, assignment.getDescription());
        values.put(DEADLINE, assignment.getCalendarString(assignment.getDeadline()));
        values.put(SUBJECT, assignment.getSubject());
        values.put(DIFFICULTY, assignment.getDifficulty());
        values.put(REMINDER, assignment.getCalendarString(assignment.getReminder()));

        int result = db.update(ASSIGNMENTS_TABLE, values, ASSIGNMENT_ID + " = " + String.valueOf(assignment.getID()), null);
        db.close();
        return result;
    }

    public int getAssignmentCount()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String countQuery = "SELECT * FROM " + ASSIGNMENTS_TABLE;

        Cursor cursor = db.rawQuery(countQuery, null);
        int result = cursor.getCount();
        db.close();
        return result;
    }

    public void deleteAllDays() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        List<Day> dayList = new ArrayList<Day>();
        dayList = getAllDays();
        for (int i = 0; i < dayList.size(); i++) {
            deleteDay(dayList.get(i).getID());
        }
        sqLiteDatabase.close();
    }

    public void createDay(Day day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DAY_DATE, day.getDateString());
        values.put(DAY_ASSIGNMENTS, day.getAssignments());
        values.put(DAY_BUSY, day.getBusy());

        db.insert(DAY_TABLE, null, values);
    }

    public void deleteDay(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DAY_TABLE, DAY_ID + " = " + String.valueOf(ID), null);
        db.close();
    }

    public Day getDay(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DAY_TABLE,
                new String[]{DAY_ID, DAY_DATE, DAY_ASSIGNMENTS, DAY_BUSY},
                DAY_ID + " = " + String.valueOf(id),
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Day day = new Day(
                cursor.getInt(0),                           //DAY_ID
                getCalendarObject(cursor.getString(1)),     //DAY_DATE
                cursor.getString(2),                        //DAY_ASSIGNMENTS
                cursor.getInt(3)                            //DAY_BUSY
        );
        db.close();
        return day;
    }

    public List<Day> getAllDays() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Day> dayList = new ArrayList<Day>();

        String selectQuery = "SELECT * FROM " + DAY_TABLE;

        Cursor cursor;
        cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Day day = new Day(
                        cursor.getInt(0),                           //DAY_ID
                        getCalendarObject(cursor.getString(1)),     //DAY_DATE
                        cursor.getString(2),                        //DAY_ASSIGNMENTS
                        cursor.getInt(3)                            //DAY_BUSY
                );

                dayList.add(day);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return dayList;
    }

    public int updateDay(Day day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DAY_DATE, day.getDateString());
        values.put(DAY_ASSIGNMENTS, day.getAssignments());
        values.put(DAY_BUSY, day.getBusy());

        int result = db.update(DAY_TABLE, values, DAY_ID + " = " + String.valueOf(day.getID()), null);
        db.close();
        return result;
    }

    public int getDayCount() {
        SQLiteDatabase db = this.getWritableDatabase();

        String countQuery = "SELECT * FROM " + DAY_TABLE;

        Cursor cursor = db.rawQuery(countQuery, null);
        int result = cursor.getCount();
        db.close();
        return result;
    }

    public int[] getDayAssignments(String string) {
        String[] assignmentString = string.split(",");
        int[] assignments = new int[10];
        for (int i = 0; i < assignmentString.length; i++) {
            assignments[i] = Integer.getInteger(assignmentString[i]);
        }
        return assignments;
    }

    public Calendar getCalendarObject(String string) {
        String[] reminderString = string.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(reminderString[0]));
        c.set(Calendar.MONTH, Integer.parseInt(reminderString[1]));
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(reminderString[2]));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(reminderString[3]));
        c.set(Calendar.MINUTE, Integer.parseInt(reminderString[4]));
        return c;
    }

}
