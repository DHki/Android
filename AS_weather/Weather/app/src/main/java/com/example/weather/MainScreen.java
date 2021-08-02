package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {

    private DBHelper helper = null;
    private SQLiteDatabase db = null;
    private ListView lv;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        try{
            helper = new DBHelper(getApplicationContext(), "database.db", null, 1);
            db = helper.getWritableDatabase();
            helper.onCreate(db);

            //helper.addLocation(db, "평택시", "62", "114");
            //helper.addLocation(db, "pyeongteak", "62", "114");
        } catch (Exception e) {}
        finally {
            db.close();
        }

        lv = (ListView) findViewById(R.id.interest_list);

        setListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String loc = "";

                try{
                    helper = new DBHelper(getApplicationContext(), "database.db", null, 1);
                    db = helper.getWritableDatabase();
                    helper.onCreate(db);
                }catch (Exception e){}

                String sql = "SELECT location FROM data";
                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();

                for(int i = 0; i < position; i++) cursor.moveToNext();
                loc = cursor.getString(0);

                //Toast.makeText(getApplicationContext(), loc, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), WeatherScreen.class);
                intent.putExtra("location", loc);

                startActivity(intent);

                db.close();
            }
        });

    }

    private void setListView(){

        try{
            helper = new DBHelper(getApplicationContext(), "database.db", null, 1);
            db = helper.getWritableDatabase();
            helper.onCreate(db);
        }catch (Exception e) {}

        String sql = "SELECT _id, location FROM data";
        cursor = db.rawQuery(sql, null);

        if(cursor.getCount() == 0){
            db.close();
            return;
        }

        String[] strings = new String[]{"location"};
        int[] ints = new int[] {android.R.id.text1};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, cursor, strings, ints, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);

        db.close();
    }

    public void reLoad(View view){
        setListView();
        Toast.makeText(getApplicationContext(), "Reloaded", Toast.LENGTH_SHORT).show();
    }
    public void addItem(View view){
        startActivity(new Intent(getApplicationContext(), AddItem.class));
    }
    public void deleteItem(View view){
        startActivity(new Intent(getApplicationContext(), DeleteItem.class));
    }
}