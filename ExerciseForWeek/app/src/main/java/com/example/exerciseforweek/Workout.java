package com.example.exerciseforweek;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Workout extends AppCompatActivity {

    String origin_exercise[] = new String[5];
    DBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        origin_exercise = getResources().getStringArray(R.array.exercise);

        setDatabase();
        setButton();
        setListView();
    }

    private void setDatabase(){
        helper = new DBHelper(getApplicationContext(), "database.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        return;
    }

    private void setButton(){
        Button reloadButton = (Button) findViewById(R.id.reload);

        reloadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                reMakeSchedule();

                Toast.makeText(getApplicationContext(), "Reloaded", Toast.LENGTH_SHORT).show();
            }
        });

        Button clearButton = (Button) findViewById(R.id.clear);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 5; i++)
                    db.delete("data", "txt = ?", new String[]{origin_exercise[i]});

                Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }

    private void reMakeSchedule(){
        boolean visit[] = new boolean[5];

        // 정해진 운동의 순서를 무작위로 바꾸는 함수
        for(int i = 0; i < 5; i++) {
            visit[i] = false;
            db.delete("data", "txt = ?", new String[]{origin_exercise[i]});
        }

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        for(int i = 0; i < 5; i++){

            while(true){
                int tmp = random.nextInt(5);

                if(!visit[tmp]){
                    ContentValues values = new ContentValues();
                    values.put("_id", i);
                    values.put("txt", origin_exercise[tmp]);
                    db.insert("data", null, values);

                    visit[tmp] = true;
                    break;
                }
            }
        }

        return;
    }

    private void setListView(){
        ListView lv = (ListView) findViewById(R.id.list);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.week, android.R.layout.simple_expandable_list_item_1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isReloaded()){
                    Toast.makeText(getApplicationContext(), "Reload first", Toast.LENGTH_SHORT).show();

                    return;
                }
                else {
                    String sql = "SELECT txt FROM data WHERE _id='" + Integer.toString(position) + "'";
                    Cursor cursor = db.rawQuery(sql, null);

                    cursor.moveToNext();
                    String popup_message = cursor.getString(0);

                    Toast.makeText(getApplicationContext(), popup_message, Toast.LENGTH_SHORT).show();

                    cursor.close();

                    return;
                }
            }
        });

        return;
    }

    private boolean isReloaded(){
        String sql = "SELECT txt FROM data";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount() == 0)
            return false;
        else
            return true;
    }

}
