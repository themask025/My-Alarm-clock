package com.example.myalarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "alarms.db";
    public static final String TABLE_NAME = "alarmslist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "DATE";
    public static final String COL3 = "NAME";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " DATE TEXT , NAME TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //adds data to the database
    public boolean addData(String date, String name) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, date);
        contentValues.put(COL3, name);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data is inserted incorrectly it will return -1
        if (result == -1) {
            return false; //error code 1 -> Problem with inserting to DB.
        } else {
            return true; //code 0 -> Inserted successfully.
        }
    }

    //returns sorted data
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL2 + " ASC", null);
    }

    //removes data by id
    public boolean removeData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_NAME, COL1 + "=" + id, null) > 0;
    }

    //removes the row with earliest/smallest date value
    public void removeFirstEntry(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL2 + " = (SELECT MIN( " + COL2 + " ) FROM "+ TABLE_NAME + ")");
    }

    //deletes the current database table and creates new
    public void cleanDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DROP TABLE " + TABLE_NAME);
        this.onCreate(db);
    }
}
