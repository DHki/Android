package com.example.weather;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DeleteItem extends AppCompatActivity {

    private ListView listView;

    private DBHelper helper = null;
    private SQLiteDatabase db = null;
    @Override
    protected void onCreate(Bundle savedInstace){
        super.onCreate(savedInstace);
        setContentView(R.layout.delete_item);

        listView = (ListView) findViewById(R.id.interest_list_delete);
        setListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    helper = new DBHelper(getApplicationContext(), "database.db", null, 1);
                    db = helper.getWritableDatabase();
                    helper.onCreate(db);
                }catch (Exception e){}

                String sql = "SELECT location FROM data";
                Cursor cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();

                for(int i = 0; i < position; i++) cursor.moveToNext();

                helper.deleteLocation(db, cursor.getString(0));

                db.close();

                Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
                setListView();
            } n
        });
    }

    private void setListView(){

        try{
            helper = new DBHelper(getApplicationContext(), "database.db", null, 1);
            db = helper.getWritableDatabase();
            helper.onCreate(db);
        }catch (Exception e) {}

        String sql = "SELECT _id, location FROM data";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount() == 0){
            db.close();
            return;
        }

        String[] strings = new String[]{"location"};
        int[] ints = new int[] {android.R.id.text1};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, cursor, strings, ints, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);

        db.close();
    }

    public void backFromDelete(View view){ finish(); }
}
