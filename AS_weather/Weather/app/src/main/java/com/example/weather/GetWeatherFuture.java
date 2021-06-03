package com.example.weather;

import android.os.Handler;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetWeatherFuture extends Thread{
    private String webUrl;
    private String serviceUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private String serviceKey = "SGekjuNyDUVjKfl26VG%2BSRQEJDGGsqsKvJTNZrZGQWcez4lllhqoKeYfnryztvVBRRWalGeP4ulEvt1Vkao%2FAg%3D%3D";
    private String time[];
    private String response = "";

    private int status = -1;
    private int sky = -1;
    private String percent;
    private String tempreture;

    public String result;
    public TextView textView;

    GetWeatherFuture(String nx, String ny, TextView textView){
        this.textView = textView;

        time = getCurrentTime();
        webUrl = serviceUrl + "?serviceKey=" + serviceKey + "&numOfRows=10&pageNo=1&base_date=" + time[0] + "&base_time=" + time[1];
        webUrl += "&nx=" + nx + "&ny=" + ny;
    }

    public void run(){
        try{
            connectWeb();
            analyzeResponse();
            result = makeContent();
        } catch (Exception e) {result = e.getMessage();}
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
        }catch (Exception e) {}
    }

    private void analyzeResponse(){
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        XmlPullParser parser = null;

        try{
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
        }catch (Exception e) {}

        String decide = "None";
        try {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("category")) {
                    parser.next();
                    String tmp = parser.getText();

                    if (tmp.equals("POP")) decide = "POP";
                    else if (tmp.equals("PTY")) decide = "PTY";
                    else if (tmp.equals("SKY")) decide = "SKY";
                    else if (tmp.equals("T3H")) decide = "T3H";
                }

                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("fcstValue")) {
                    parser.next();

                    if (decide.equals("POP")) percent = parser.getText();
                    else if (decide.equals("PTY")) status = Integer.parseInt(parser.getText());
                    else if (decide.equals("SKY")) sky = Integer.parseInt(parser.getText());
                    else if (decide.equals("T3H")) tempreture = parser.getText();

                    decide = "None";
                }
            }
        }catch (Exception e) {}
    }

    private String makeContent(){
        String tmp = "<예보>\n\n";

        tmp += "3시간 기온: " + tempreture + "℃\n\n";
        tmp += "하늘 상태: ";
        switch (sky){
            case 1:
                tmp += "맑음\n\n";
                break;
            case 3:
                tmp += "구름 많음\n\n";
                break;
            case 4:
                tmp += "흐림\n\n";
                break;
            default:
                tmp += "XML파싱 에러\n\n";
                break;
        }

        tmp += "강수 확률: " + percent + "%\n\n";
        tmp += "강수 형태: ";
        switch (status){
            case 0:
                tmp += "없음\n\n";
                break;
            case 1:
                tmp += "비\n\n";
                break;
            case 2:
                tmp += "비/눈\n\n";
                break;
            case 3:
                tmp += "눈\n\n";
                break;
            case 4:
                tmp += "소나기\n\n";
                break;
            case 5:
                tmp += "빗방울\n\n";
                break;
            case 6:
                tmp += "빗방울/눈날림\n\n";
                break;
            case 7:
                tmp += "눈날림\n\n";
                break;
            default:
                tmp += "XML 파싱 에러\n\n";
                break;
        }

        return tmp;
    }
}
