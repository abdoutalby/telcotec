package com.example.telcotec.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.CallLog;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Telcotec.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("Create Table calls (id INTEGER primary key AUTOINCREMENT ,date Date , number Text , name Text , duration Text , type Text )");

        DB.execSQL("Create Table ftp (id INTEGER primary key AUTOINCREMENT , date Text ,result Text  )");

        DB.execSQL("Create Table streaming (id INTEGER primary key AUTOINCREMENT ,  duree Text ,temps Text )");

        DB.execSQL("Create Table web (id INTEGER primary key AUTOINCREMENT ,  time Text  ,date Text , heure Text)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists calls");
        DB.execSQL("drop Table if exists ftp");
        DB.execSQL("drop Table if exists streaming");
        DB.execSQL("drop Table if exists web");
    }

    public Boolean saveCall(CallData call) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", (byte[]) null);
        contentValues.put("date", String.valueOf(call.getDate()));
        contentValues.put("number", call.getNumber());
        contentValues.put("name", call.getName());
        contentValues.put("duration", call.getDuration());
        contentValues.put("type", call.getType());


        long res = DB.insert("calls", null, contentValues);

        if (res == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Boolean saveftp(String date , String result) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", (byte[]) null);
        contentValues.put("date", date);
        contentValues.put("result", result);
        long res = DB.insert("ftp", null, contentValues);

        if (res == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Boolean saveweb(String time    ,String date  ,String heure) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", (byte[]) null);
        contentValues.put("time", time);
        contentValues.put("date", date);
        contentValues.put("heure", heure);
        long res = DB.insert("web", null, contentValues);

        if (res == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Boolean savestreaming(String duree  ,String temps) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", (byte[]) null);
         contentValues.put("duree", duree);
        contentValues.put("temps", temps);
        long res = DB.insert("streaming", null, contentValues);

        if (res == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getCallHistory() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor managedCursor = DB.rawQuery("Select * from calls", null);
        return  managedCursor;
    }
}
