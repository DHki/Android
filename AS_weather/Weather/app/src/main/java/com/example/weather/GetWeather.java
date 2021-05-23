package com.example.weather;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetWeather{
    private String webUrl; // 접속해야 할 url을 저장합니다.
    private String serviceUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
    private String serviceKey = "SGekjuNyDUVjKfl26VG%2BSRQEJDGGsqsKvJTNZrZGQWcez4lllhqoKeYfnryztvVBRRWalGeP4ulEvt1Vkao%2FAg%3D%3D";

    private String time[]; // 현재 시간에 대한 정보를 저장할 배열입니다.

    private StringBuilder builder; // 네트워크에 접속하고, xml을 저장할 객체입니다.

    GetWeather(String nx, String ny){
        time = getCurrentTime();
        webUrl = serviceUrl + "?serviceKey=" + serviceKey + "&numOfRows=10&pageNo=1&base_date=" + time[0] + "&base_time=" + time[1];
        webUrl += "&nx=" + nx + "&ny=" + ny;
    }

    private String[] getCurrentTime(){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format2 = new SimpleDateFormat("HHmm");
        Date time = new Date(System.currentTimeMillis());

        String ret[] = {format1.format(time), format2.format(time)};
        if(ret[1].charAt(2) >= '4'){
            SimpleDateFormat tmp = new SimpleDateFormat("HH");
            ret[1] = tmp.format(time) + "00";
        }
        else{
            SimpleDateFormat tmp = new SimpleDateFormat("HH");
            if(tmp.equals("00")){
                ret[1] = (tmp + "00");
            }
            else {
                ret[1] = Integer.toString(Integer.parseInt(tmp.format(time)) - 1) + "00";
            }
        }

        return ret;
    }

    public void connectWeb(){
        try {
            URL url = new URL(webUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;

            while((line = reader.readLine()) != null) builder.append(line + "\n");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void analyzeData(){

    }
}
