package com.miguel.ximbal.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(email text primary key, password text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");

    }
    //Inserting in database
    public boolean insert(String email,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins = db.insert("user", null, contentValues);
        if (ins == -1) return false;
        else return
                true;
    }
     // checking if email exists
    public Boolean checkemail(String email) {
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor cursor = db.rawQuery("Select * from user where email=?", new String[]{email});
         if (cursor.getCount()>0) return false;
         else return true;
        }
    // checking if password exists
    public Boolean checkpass(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where password=?", new String[]{password});
        if (cursor.getCount()>0) return false;
        else return true;
    }
    }

