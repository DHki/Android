package com.example.findtresure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class Mode_Play extends View {
    Context context;

    private int width;
    private int height;

    private Bitmap background;
    private int background_y;

    private Treasure treasure;
    private boolean treasure_check = false;

    private Bitmap end_button;
    private boolean end_button_check = false;
    private int end_button_width, end_button_x, end_button_y;

    private int cnt = 0;
    private String text = "터치 횟수 : ";

    private int integer_tmp = 0;
    private String string_tmp = "포기 버튼 -->";

    private GameThread thread_play;

    private Paint paint = new Paint();

    Mode_Play(Context context){
        super(context);
        this.context = context;

        paint.setColor(Color.WHITE);
        paint.setTextSize(62);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.play_background_00 + random.nextInt(4));
        background = Bitmap.createScaledBitmap(background, width, width, true);
        background_y = height / 2 - (width / 2);

        treasure = new Treasure();
        treasure.width = width / 7;
        treasure.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.treasure_01);
        treasure.image = Bitmap.createScaledBitmap(treasure.image, treasure.width, treasure.width, true);

        treasure.x = random.nextInt(width - treasure.width);
        treasure.y = random.nextInt(width - treasure.width) + background_y;

        end_button = BitmapFactory.decodeResource(context.getResources(), R.drawable.return_button_01);
        end_button_width = width / 6;
        end_button_x = (width / 6) * 5;
        end_button_y = background_y + width / 6 * 7;
        end_button = Bitmap.createScaledBitmap(end_button, end_button_width, end_button_width, true);

        if(thread_play == null){
            thread_play = new GameThread();
            thread_play.start();
        }
    }
    Mode_Play(Context context, Bitmap back, int treasureX, int treasureY){
        super(context);
        this.context = context;

        paint.setColor(Color.WHITE);
        paint.setTextSize(62);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        background = back;
        background_y = height / 2 - (width / 2);

        treasure = new Treasure();
        treasure.width = width / 7;
        treasure.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.treasure_01);
        treasure.image = Bitmap.createScaledBitmap(treasure.image, treasure.width, treasure.width, true);
        treasure.x = treasureX;
        treasure.y = treasureY;

        end_button = BitmapFactory.decodeResource(context.getResources(), R.drawable.return_button_01);
        end_button_width = width / 6;
        end_button_x = (width / 6) * 5;
        end_button_y = background_y + width / 6 * 7;
        end_button = Bitmap.createScaledBitmap(end_button, end_button_width, end_button_width, true);

        if(thread_play == null){
            thread_play = new GameThread();
            thread_play.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.BLACK);

        canvas.drawBitmap(background, 0, background_y, paint);
        canvas.drawText("보물을 찾아주세요 !!", width / 20, background_y - width / 5, paint);
        canvas.drawText(text + Integer.toString(cnt), width / 20, background_y - width / 10, paint);
        canvas.drawBitmap(end_button, end_button_x, end_button_y, paint);

        if(treasure_check){
            canvas.drawBitmap(treasure.image, treasure.x, treasure.y, paint);
        }

        if(integer_tmp < 150){
            canvas.drawText(string_tmp, end_button_x - 2 * end_button_width, end_button_y + end_button_width / 5 * 3, paint);
            integer_tmp++;

            if(integer_tmp == 150){
                string_tmp = "종료 버튼 -->";
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = 0;
        float y = 0;

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            x = event.getX();
            y = event.getY();

            if(background_y < y && y < background_y + width && !treasure_check){
                cnt++;
            }
        }

        if(treasure.x < x && x < treasure.x + treasure.width && treasure.y < y && y < treasure.y + treasure.width){
            treasure_check = true;
            if(!end_button_check){
                integer_tmp = 0;
            }
            end_button_check = true;
        }

        if(end_button_x < x && x < end_button_x + end_button_width && end_button_y < y && y < end_button_y + end_button_width){
            if(end_button_check){
                Activity activity = (Activity) context;
                activity.finish();
            }
            else{
                end_button_check = true;
                treasure_check = true;
                integer_tmp = 0;
            }
        }

        return false;
    }

    @Override
    protected void onDetachedFromWindow(){
        thread_play.operation = false;
        super.onDetachedFromWindow();
    }

    private class GameThread extends Thread{
        public boolean operation = true;
        GameThread(){}

        @Override
        public void run(){
            while(operation){

                try{
                    postInvalidate();
                    sleep(10);
                }
                catch(Exception e){}
            }
        }
    }
}
