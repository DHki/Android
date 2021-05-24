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
import java.util.concurrent.TimeUnit;

public class WeatherScreen extends AppCompatActivity {
    ExecutorService service;
    Executor executer;

    GetWeather weather;

    String location[], nx[], ny[];
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_screen);

        setDefault();
        setText();
    }

    private void setDefault(){
        service = Executors.newFixedThreadPool(4);

        index = getIntent().getIntExtra("index", -1);
        location = getResources().getStringArray(R.array.location);
        nx = getResources().getStringArray(R.array.nx);
        ny = getResources().getStringArray(R.array.ny);

        // 먼저 새로운 스레드로 인터넷에 연결하도록 해야합니다.
        weather = new GetWeather(nx[index], ny[index]);
        executer.execute(new Runnable() {
            @Override
            public void run() {
                weather.connectWeb();
            }
        });

        // 그 뒤에 weather.analyze()를 한 뒤에 화면에 출력하도록 해야함.
        try{
            if(service.awaitTermination(5, TimeUnit.SECONDS)){
                executer.execute(new Runnable() {
                    @Override
                    public void run() {
                        weather.analyzeData();
                    }
                });
            }
            else{
                Toast.makeText(this, "문제가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) { e.printStackTrace(); }

        TextView title = (TextView) findViewById(R.id.title_weather);
        String tmp_title = location[index] + "의 현재 날씨";
        title.setText(tmp_title);
    }

    private void setText(){
        try{
            if(service.awaitTermination(5, TimeUnit.SECONDS)){

            }
            else{
                Toast.makeText(this, "문제가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){ e.printStackTrace(); }
    }
}
