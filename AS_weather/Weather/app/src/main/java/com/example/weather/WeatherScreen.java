package com.example.weather;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WeatherScreen extends AppCompatActivity {
    ExecutorService service;

    GetWeatherCurrent weatherCurrent;
    // GetWeatherFuture weatherFuture;

    String location[], nx[], ny[];
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        service = Executors.newFixedThreadPool(4);

        setContentView(R.layout.weather_screen);

        setDefault();
    }

    private void setDefault(){
        index = getIntent().getIntExtra("index", -1);
        location = getResources().getStringArray(R.array.location);
        nx = getResources().getStringArray(R.array.nx);
        ny = getResources().getStringArray(R.array.ny);

        // 먼저 새로운 스레드로 인터넷에 연결하도록 해야합니다.
        weatherCurrent = new GetWeatherCurrent(nx[index], ny[index], (TextView) findViewById(R.id.weather_content_current));
        weatherCurrent.start();

        //weatherFuture = new GetWeatherFuture;
        //weatherFuture.start();

        Toast.makeText(this, "Connecting started", Toast.LENGTH_SHORT).show();


        TextView title = (TextView) findViewById(R.id.title_weather);
        String tmp_title = location[index] + "의 현재 날씨";
        title.setText(tmp_title);
    }

    public void back(View view){ finish(); }
}
