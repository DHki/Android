package com.example.weather;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherScreen extends AppCompatActivity {
    ExecutorService executorService;
    GetWeather weather;
    String location[], nx[], ny[];
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_screen);

        setDefault();
    }

    private void setDefault(){
        executorService = Executors.newFixedThreadPool(4);
        index = getIntent().getIntExtra("index", -1);
        location = getResources().getStringArray(R.array.location);
        nx = getResources().getStringArray(R.array.nx);
        ny = getResources().getStringArray(R.array.ny);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                weather = new GetWeather(nx[index], ny[index]);
            }
        });

        TextView title = (TextView) findViewById(R.id.title_weather);
        String tmp_title = location[index] + "의 현재 날씨";
        title.setText(tmp_title);
    }
}
