package com.example.exerciseforweek;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String string, SQLiteDatabase.CursorFactory factory, int version){
        super(context, string, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE IF NOT EXISTS data (_id INTEGER, txt text);";
        db.execSQL(sql);

        return;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        return;
    }
}
