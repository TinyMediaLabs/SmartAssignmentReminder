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
    private static final String DUEDATE                 = "dueDate";
    private static final String SUBJECT                 = "subject";

    //Cards table definition
    private static final String CARDS_TABLE        = "cards";
    private static final String CARD_ID            = "ID";
    private static final String CARD_TITLE         = "title";
    private static final String CARD_DESCRIPTION   = "description";
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
                + DUEDATE + " REAL,"
                + SUBJECT + " TEXT"
                + ")";

        String CREATE_CARDS_TABLE = "" +
                "CREATE TABLE " + CARDS_TABLE
                + " ("
                + CARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CARD_TITLE + " TEXT,"
                + CARD_DESCRIPTION + " TEXT,"
                + CARD_SUBJECT + " TEXT"
                + ")";

        db.execSQL(CREATE_ASSIGNMENTS_TABLE);
        db.execSQL(CREATE_CARDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENTS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CARDS_TABLE);
        onCreate(sqLiteDatabase);
    }

    void addAssignment(Assignment assignment)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSIGNMENT_TITLE,       assignment.getTitle());
        values.put(ASSIGNMENT_DESCRIPTION, assignment.getDescription());
        values.put(DUEDATE,                assignment.getDueDate());
        values.put(SUBJECT,                assignment.getSubject());

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
                new String[] {ASSIGNMENT_ID, ASSIGNMENT_TITLE, ASSIGNMENT_DESCRIPTION, DUEDATE, SUBJECT},
                ASSIGNMENT_ID + " = " + String.valueOf(id),
                null, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Assignment assignment= new Assignment(
                cursor.getInt(0),       //ASSIGNMENT_ID
                cursor.getString(1),    //ASSIGNMENT_TITLE
                cursor.getString(2),    //ASSIGNMENT_DESCRIPTION
                cursor.getDouble(3),    //DUEDATE
                cursor.getString(4)     //SUBJECT
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
                        cursor.getInt(0),       //ASSIGNMENT_ID
                        cursor.getString(1),    //ASSIGNMENT_TITLE
                        cursor.getString(2),    //ASSIGNMENT_DESCRIPTION
                        cursor.getDouble(3),    //DUEDATE
                        cursor.getString(4)     //SUBJECT
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
        values.put(DUEDATE,                assignment.getDueDate());
        values.put(SUBJECT,                assignment.getSubject());

        return db.update(ASSIGNMENTS_TABLE, values, ASSIGNMENT_ID + " = " + String.valueOf(assignment.getID()), null);
    }

    public int getAssignmentCount()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String countQuery = "SELECT + FROM " + ASSIGNMENTS_TABLE;

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

    void addCard (Card card)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CARD_TITLE,  card.getTitle());
        values.put(CARD_DESCRIPTION, card.getDescription());
        values.put(CARD_SUBJECT, card.getSubject());

        db.insert(CARDS_TABLE, null, values);
    }

    void deleteCard(Card card)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CARDS_TABLE, CARD_ID + " = " + String.valueOf(card.getID()), null);
    }

    public Card getCard(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                CARDS_TABLE,
                new String[]{CARD_ID, CARD_TITLE, CARD_DESCRIPTION, CARD_SUBJECT},
                CARD_ID + " = " + String.valueOf(id),
                null, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Card card = new Card(
                cursor.getInt(0),       //CARD_ID
                cursor.getString(1),    //CARD_TITLE
                cursor.getString(2),    //CARD_DESCRIPTION
                cursor.getString(3)     //CARD_SUBJECT
        );
        return card;

    }

    public List<Card> getAllCards()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Card> cardList = new ArrayList<Card>();

        String selectQuery = "SELECT + FROM " + CARDS_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                Card card = new Card(
                        cursor.getInt(0),       //CARD_ID
                        cursor.getString(1),    //CARD_TITLE
                        cursor.getString(2),    //CARD_DESCRIPTION
                        cursor.getString(3)     //CARD_SUBJECT
                );

                cardList.add(card);
            }
            while (cursor.moveToFirst());
        }

        return cardList;

    }

    public int updateCard(Card card)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CARD_TITLE,       card.getTitle());
        values.put(CARD_DESCRIPTION, card.getDescription());

        return db.update(CARDS_TABLE, values, CARD_ID + " = " + String.valueOf(card.getID()), null);
    }

    public int getCardsCount()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String countQuery = "SELECT + FROM " + CARDS_TABLE;

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
