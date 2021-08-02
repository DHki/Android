package com.example.weather;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String string, SQLiteDatabase.CursorFactory factory, int version){
        super(context, string, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE IF NOT EXISTS data (_id INTEGER PRIMARY KEY AUTOINCREMENT, nx TEXT, ny TEXT, location TEXT)";
        db.execSQL(sql);

        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        return;
    }

    public String[] searchLocation(SQLiteDatabase db, String loc){
        String ret[] = new String[2];

        Cursor cursor = null;

        String sql = "SELECT nx, ny FROM data WHERE location='" + loc + "'";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        ret[0] = cursor.getString(0);
        ret[1] = cursor.getString(1);

        return ret;
    }

    public void addLocation(SQLiteDatabase db, String loc, String x, String y){
        String sql="INSERT INTO data (nx, ny, location) VALUES (" + "'" + x + "', " + "'" + y + "', " + "'" + loc + "')";
        db.execSQL(sql);
    }

    public void deleteLocation(SQLiteDatabase db, String loc){
        String sql = "DELETE FROM data WHERE location='" + loc + "'";
        db.execSQL(sql);
    }

    public void deleteAll(SQLiteDatabase db){
        String sql = "DELETE FROM data";
        db.execSQL(sql);
    }
}
