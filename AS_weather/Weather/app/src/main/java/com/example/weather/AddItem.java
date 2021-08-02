package com.example.weather;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItem extends AppCompatActivity {

    private DBHelper helper = null;
    private SQLiteDatabase db = null;
    private ListView listView;

    private boolean check = false;
    private int loc = -1;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.add_item);

        listView = (ListView) findViewById(R.id.interest_list_add);
        setListView(-1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!check){
                    check = true;
                    loc = position;
                    setListView(position);
                }
                else if(check){
                    switch (loc){
                        case 0:
                            String location0 = getResources().getStringArray(R.array.loc_0)[position];
                            String nx0 = getResources().getStringArray(R.array.x_0)[position];
                            String ny0 = getResources().getStringArray(R.array.y_0)[position];
                            addLoc(location0, nx0, ny0);
                            break;
                        case 8:
                            String location8 = getResources().getStringArray(R.array.loc_8)[position];
                            String nx8 = getResources().getStringArray(R.array.x_8)[position];
                            String ny8 = getResources().getStringArray(R.array.y_8)[position];
                            addLoc(location8, nx8, ny8);
                            break;

                    }

                    Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void setListView(int version){
        switch (version){
            case -1:
                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.location, android.R.layout.simple_expandable_list_item_1);
                listView.setAdapter(adapter);
                break;
            case 0:
                ArrayAdapter adapter0 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.loc_0, android.R.layout.simple_expandable_list_item_1);
                listView.setAdapter(adapter0);
                break;
            case 8:
                ArrayAdapter adapter8 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.loc_8, android.R.layout.simple_expandable_list_item_1);
                listView.setAdapter(adapter8);
                break;
        }
    }

    private void addLoc(String location, String nx, String ny){
        try{
            helper = new DBHelper(getApplicationContext(), "database.db", null, 1);
            db = helper.getWritableDatabase();
            helper.onCreate(db);
        }catch (Exception e) {}

        helper.addLocation(db, location, nx, ny);

        db.close();
    }

    public void backFromAdd(View view){
        finish();
    }
}
