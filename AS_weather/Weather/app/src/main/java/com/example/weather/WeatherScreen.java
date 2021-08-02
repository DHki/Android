package com.example.weather;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    private ExecutorService service;

    private DBHelper helper = null;
    private SQLiteDatabase db = null;

    private GetWeatherCurrent weatherCurrent;
    private GetWeatherFuture weatherFuture;

    private String location, nx, ny;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        service = Executors.newFixedThreadPool(4);

        setContentView(R.layout.weather_screen);

        setData();
    }

    private void setData(){
        try{
            helper = new DBHelper(getApplicationContext(), "database.db", null, 1);
            db = helper.getReadableDatabase();
        }catch (Exception e){}

        location = getIntent().getStringExtra("location");
        String tmp[] = helper.searchLocation(db, location);
        nx = tmp[0]; ny = tmp[1];

        db.close();


        // 먼저 새로운 스레드로 인터넷에 연결하도록 해야합니다.
        weatherCurrent = new GetWeatherCurrent(nx, ny, (TextView) findViewById(R.id.weather_content_current));
        weatherCurrent.start();

        weatherFuture = new GetWeatherFuture(nx, ny, (TextView) findViewById(R.id.weather_content_future));
        weatherFuture.start();

        TextView title = (TextView) findViewById(R.id.title_weather);
        String tmp_title = location + "의 날씨";
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

        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    public void backFromWeather(View view){ finish(); }
}
