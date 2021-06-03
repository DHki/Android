package com.example.weather;



import android.util.Xml;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetWeatherCurrent extends Thread{
    private String webUrl; // 접속해야 할 url을 저장합니다.
    private String serviceUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
    private String serviceKey = "SGekjuNyDUVjKfl26VG%2BSRQEJDGGsqsKvJTNZrZGQWcez4lllhqoKeYfnryztvVBRRWalGeP4ulEvt1Vkao%2FAg%3D%3D";
    private String time[]; // 현재 시간에 대한 정보를 저장할 배열입니다.
    private String content = "";

    public String tempreture = "";
    public int status = -1;
    public String humidity = "";
    public String precipitation = "";
    public String windSpeed = "";

    private TextView textView;
    GetWeatherCurrent(String nx, String ny, TextView textView){
        this.textView = textView;

        time = getCurrentTime();
        webUrl = serviceUrl + "?serviceKey=" + serviceKey + "&numOfRows=10&pageNo=1&base_date=" + time[0] + "&base_time=" + time[1];
        webUrl += "&nx=" + nx + "&ny=" + ny;
    }

    public void run(){
        connectWeb();
        try{
            analyzeData();
        }catch (Exception e){}

        String content = makeContent();
        textView.setText(content);
    }

    private static String[] getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        Date time = new Date(System.currentTimeMillis());

        String ret[] = {format.format(time).substring(0, 8), format.format(time).substring(8)};

        if(Integer.parseInt(ret[1].substring(2)) < 40){
            String clock = ret[1].substring(0, 2);
            if(clock.equals("00")) {
                time = new Date();
                time = new Date(time.getTime() + (1000*60*60*24 - 1));

                ret[0] = format.format(time).substring(0, 8);
                ret[1] = "2300";
            }
            else if(clock.equals("01")){
                ret[1] = "0000";
            }
            else {
                int timeTmp = Integer.parseInt(clock) - 1;
                if(timeTmp >= 10) ret[1] = Integer.toString(timeTmp) + "00";
                else ret[1] = "0" + Integer.toString(timeTmp) + "00";
            }
        }
        else ret[1] = ret[1].substring(0, 2) + "00";

        return ret;
    }

    private void connectWeb(){
        try {
            URL url = new URL(webUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";

            while((line = reader.readLine()) != null) content += line;
        }catch (Exception e) {
            e.printStackTrace();
            content = e.getMessage();
        }
    }

    private void analyzeData()throws XmlPullParserException, IOException {
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        XmlPullParser parser = null;

        try{
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.next();
        }catch (Exception e){}

        int cnt = 0;
        while(parser.next() != XmlPullParser.END_DOCUMENT){

            if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("obsrValue")) {

                parser.next();

                switch (cnt) {
                    case 0:
                        status = Integer.parseInt(parser.getText());
                        break;
                    case 1:
                        humidity = parser.getText();
                        break;
                    case 2:
                        precipitation = parser.getText();
                        break;
                    case 3:
                        tempreture = parser.getText();
                        break;
                    case 7:
                        windSpeed = parser.getText();
                        break;
                }
                cnt++;
            }
        }
    }

    private String makeContent(){
        String tmp = "\n";

        tmp += "온도: " + tempreture + "℃\n\n\n";
        tmp += "강수 형태: ";
        switch (status){
            case 0:
                tmp += "없음\n\n\n";
                break;
            case 1:
                tmp += "비\n\n\n";
                break;
            case 2:
                tmp += "비/눈\n\n\n";
                break;
            case 3:
                tmp += "눈\n\n\n";
                break;
            case 4:
                tmp += "소나기\n\n\n";
                break;
            case 5:
                tmp += "빗방울\n\n\n";
                break;
            case 6:
                tmp += "빗방울/눈날림\n\n\n";
                break;
            case 7:
                tmp += "눈날림\n\n\n";
                break;
            default:
                tmp += "XML 파싱 에러\n\n\n";
                break;
        }
        tmp += "습도: " + humidity + "%\n\n\n";
        tmp += "강수량: " + precipitation + "mm\n\n\n";
        tmp += "풍속: " + windSpeed + "m/s";

        return tmp;
    }
}
