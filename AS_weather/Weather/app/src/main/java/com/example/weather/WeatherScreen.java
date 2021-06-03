package com.example.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import java.util.logging.LogRecord;

public class WeatherScreen extends AppCompatActivity {
    ExecutorService service;
    Handler handler;

    GetWeatherCurrent weatherCurrent;
    GetWeatherFuture weatherFuture;

    String location[], nx[], ny[];
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        service = Executors.newFixedThreadPool(4);
        handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(String msg, TextView textView){
                textView.setText(msg);
            }
        };

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

        weatherFuture = new GetWeatherFuture(nx[index], ny[index], (TextView) findViewById(R.id.weather_content_future));
        weatherFuture.start();

        TextView title = (TextView) findViewById(R.id.title_weather);
        String tmp_title = location[index] + "의 날씨";
        title.setText(tmp_title);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            if(weatherCurrent.getState() == Thread.State.TERMINATED && weatherFuture.getState() == Thread.State.TERMINATED){
                                weatherCurrent.textView.setText(weatherCurrent.result);
                                weatherFuture.textView.setText(weatherFuture.result);

                                break;
                            }
                        }
                    }
                });
            }
        }).start();

        Toast.makeText(this, "Connecting", Toast.LENGTH_SHORT).show();
    }

    public void back(View view){ finish(); }
}
