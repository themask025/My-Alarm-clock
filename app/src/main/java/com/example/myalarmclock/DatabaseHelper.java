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

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getRowData(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + COL1 + " FROM " + TABLE_NAME + " WHERE " + COL2 + " ('"+ date + "')", null);
    }

    /*
    public boolean updateData(String date, String name, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(db.delete(TABLE_NAME, COL1 + "=" + id, null)<=0)
            return false;
        SQLiteDatabase db1 = this.getWritableDatabase();
        contentValues.put(COL1, id);
        contentValues.put(COL2, date);
        contentValues.put(COL3, name);
        long result = db1.insert(TABLE_NAME, null, contentValues);

        //if data is inserted incorrectly it will return -1
        if (result == -1) {
            return false; //error code 1 -> Problem with inserting to DB.
        } else {
            return true; //code 0 -> Inserted successfully.
        }
    }

     */



    public boolean removeData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL1 + "=" + id, null) > 0;
    }

    public void removeLastEntry(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = (SELECT MAX( " + COL1 + " ) FROM "+ TABLE_NAME + ")");
    }

    public void cleanDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DROP TABLE " + TABLE_NAME);
        this.onCreate(db);
    }
}
