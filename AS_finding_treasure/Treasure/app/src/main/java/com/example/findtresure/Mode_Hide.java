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

public class Mode_Hide extends View {
    private Context context;

    private GameThread thread_hide;

    private Paint paint;
    private int width, height;
    private String text = "보물을 숨긴 뒤, 아래 버튼을 눌러주세요 !!";

    static public Bitmap background;
    static public int background_y;

    private Bitmap next;
    private Treasure treasure = new Treasure();
    private boolean check_treasure = false;

    Mode_Hide(Context context){
        super(context);
        this.context = context;

        setDefault();

        if(thread_hide == null){
            thread_hide = new GameThread();
            thread_hide.start();
        }
    }

    private void setDefault(){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(62);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.play_background_00 + random.nextInt(4));
        background = Bitmap.createScaledBitmap(background, width, width, true);
        background_y = height / 2 - (width / 2);

        treasure.width = width / 7;
        treasure.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.treasure_01);
        treasure.image = Bitmap.createScaledBitmap(treasure.image, treasure.width, treasure.width, true);

        next = BitmapFactory.decodeResource(context.getResources(), R.drawable.next_button);
        next = Bitmap.createScaledBitmap(next, width / 6, width / 6, true);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.BLACK);

        canvas.drawBitmap(background, 0, background_y, paint);
        if(check_treasure){
            canvas.drawBitmap(treasure.image, treasure.x, treasure.y, paint);
        }

        canvas.drawText(text, width / 35, background_y - width / 5, paint);

        canvas.drawBitmap(next, (width / 6) * 5, background_y + width / 6 * 7, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = 0;
        float y = 0;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();
        }

        if(0 <= x && x <= width && background_y <= y && y <= background_y + width) {
            if (!check_treasure) {
                treasure.x = (int) x - (treasure.width / 2);
                treasure.y = (int) y - (treasure.width / 2);

                check_treasure = true;
            } else if (treasure.x < x && x < treasure.x + treasure.width && treasure.y < y && y < treasure.y + treasure.width) {
                check_treasure = false;
            }
        }
        else if((width / 6) * 5 < x && x < width && background_y +width / 6 * 7 < y && y < background_y + width / 6 * 8 && check_treasure){
            thread_hide.operation = false;
            Activity activity = (Activity) context;
            activity.setContentView(new Mode_Play(context, background, treasure.x, treasure.y));
        }

        return false;
    }

    @Override
    protected void onDetachedFromWindow(){
        thread_hide.operation = false;
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
                    sleep(8);
                }
                catch(Exception e){}
            }
        }
    }
}

