package com.example.weather;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetWeather{
    private String webUrl;
    private String serviceUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
    private String serviceKey = "SGekjuNyDUVjKfl26VG%2BSRQEJDGGsqsKvJTNZrZGQWcez4lllhqoKeYfnryztvVBRRWalGeP4ulEvt1Vkao%2FAg%3D%3D";

    public String time[];

    GetWeather(String nx, String ny){
        time = getCurrentTime();
        webUrl = serviceUrl + "?serviceKey=" + serviceKey + "&numOfRows=10&pageNo=1&base_date=" + time[0] + "&base_time=" + time[1];
        webUrl += "&nx=" + nx + "&ny=" + ny;

        connectWeb();
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

    private void connectWeb(){
        HttpURLConnection conn = null;
        String data = "";

        try{
            URL url = new URL(webUrl);
            conn = (HttpURLConnection) url.openConnection();

            BufferedInputStream buffer = new BufferedInputStream(conn.getInputStream());
            BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(buffer, "utf-8"));

            String line = null;
            while((line = buffer_reader.readLine()) != null){
                data += line;
            }
        }
        catch (Exception e){}
        finally {
            conn.disconnect();
        }

        analyzeData(data);
    }

    private void analyzeData(String data){
        
    }
}
