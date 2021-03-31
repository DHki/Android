package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);


        setDefault();
    }


    private void setDefault(){
        ListView lv = (ListView) findViewById(R.id.interest_list);

        ArrayAdapter adapter =ArrayAdapter.createFromResource(getApplicationContext(), R.array.location, android.R.layout.simple_expandable_list_item_1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WeatherScreen.class);
                intent.putExtra("index", position);

                startActivity(intent);
            }
        });
    }
}