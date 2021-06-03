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

public class GetWeatherFuture extends Thread{
    private String webUrl;
    private String serviceUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private String serviceKey = "SGekjuNyDUVjKfl26VG%2BSRQEJDGGsqsKvJTNZrZGQWcez4lllhqoKeYfnryztvVBRRWalGeP4ulEvt1Vkao%2FAg%3D%3D";
    private String time[];
    private String response;
    private TextView textView;

    private int status = -1;
    private int sky = -1;
    private String percent;
    private String maxTempreture;
    private String minTempreture;

    GetWeatherFuture(String nx, String ny, TextView textView){
        this.textView = textView;

        time = getCurrentTime();
        webUrl = serviceUrl + "?serviceKey=" + serviceKey + "&numOfRows=10&pageNo=1&base_date=" + time[0] + "&base_time=" + time[1];
        webUrl += "&nx=" + nx + "&ny=" + ny;
    }

    public void run(){
        try{
            connectWeb();
            //analyzeResponse();
        }catch (Exception e){}

        //String content = makeContent();
        //textView.setText(content);
    }

    private String[] getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        Date time = new Date(System.currentTimeMillis());

        String ret[] = {format.format(time).substring(0, 8), format.format(time).substring(8)};

        if(Integer.parseInt(ret[1].substring(2)) >= 10) {
            int tmp = Integer.parseInt(ret[1].substring(0, 2));

            if(tmp >= 23) ret[1] = "2300";
            else if(tmp >= 20) ret[1] = "2000";
            else if(tmp >= 17) ret[1] = "1700";
            else if(tmp >= 14) ret[1] = "1400";
            else if(tmp >= 11) ret[1] = "1100";
            else if(tmp >= 8) ret[1] = "0800";
            else if(tmp >= 5) ret[1] = "0500";
            else if(tmp >= 2) ret[1] = "0200";
            else {
                time = new Date();
                time = new Date(time.getTime() + (1000*60*60*24 - 1));

                ret[0] = format.format(time).substring(0, 8);
                ret[1] = "2300";
            }

        }
        else {
            int tmp = Integer.parseInt(ret[1].substring(0, 2));

            if(tmp >= 21) ret[1] = "2000";
            else if(tmp >= 18) ret[1] = "1700";
            else if(tmp >= 15) ret[1] = "1400";
            else if(tmp >= 12) ret[1] = "1100";
            else if(tmp >= 9) ret[1] = "0800";
            else if(tmp >= 6) ret[1] = "0500";
            else if(tmp >= 3) ret[1] = "0200";
            else {
                time = new Date();
                time = new Date(time.getTime() + (1000*60*60*24 - 1));

                ret[0] = format.format(time).substring(0, 8);
                ret[1] = "2300";
            }
        }

        return ret;
    }

    private void connectWeb(){
        try {
            URL url = new URL(webUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";

            while((line = reader.readLine()) != null) response += line;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* private void analyzeData() throws XmlPullParserException, IOException {
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
    }*/
}
