package com.example.balloonmathgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MyView extends View {
    int width, height;

    Bitmap screen;

    Bitmap basketImg;
    Basket basket;
    int basketWidth, basketHeight;

    Bitmap leftKey, rightKey;
    int leftKey_x, leftKey_y, rightKey_x, rightKey_y;

    int score = 0;
    int buttonWidth;

    Bitmap balloonImg;
    int balloonWidth, balloonHeight;

    AnswerBalloon answerBalloon;

    int count = 0;

    ArrayList<Balloon> balloonArrayList;

    int num1, num2;
    int ans;
    int[] worngNumber = new int[5];

    MyView(Context context, int width, int height){
        super(context);

        this.width = width; this.height = height;

        basket = new Basket(width /2, height * 11 / 14, 20);
        balloonArrayList = new ArrayList<Balloon>();

        basketImg = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        int x = width / 7, y = height / 14;
        basketImg = Bitmap.createScaledBitmap(basketImg, x, y, true);
        basketWidth = basketImg.getWidth(); basketHeight = basketImg.getHeight();


        buttonWidth = width / 12;
        leftKey = BitmapFactory.decodeResource(getResources(), R.drawable.leftkey);
        leftKey_x = width * 5 / 9;
        leftKey_y = height * 7 / 9;
        leftKey = Bitmap.createScaledBitmap(leftKey, buttonWidth, buttonWidth, true);
        rightKey = BitmapFactory.decodeResource(getResources(), R.drawable.rightkey);
        rightKey_x = width * 7 / 9;
        rightKey_y = height * 7 / 9;
        rightKey = Bitmap.createScaledBitmap(rightKey, buttonWidth, buttonWidth, true);

        balloonImg = BitmapFactory.decodeResource(getResources(), R.drawable.balloon);
        balloonImg = Bitmap.createScaledBitmap(balloonImg, buttonWidth, buttonWidth, true);

        screen = BitmapFactory.decodeResource(getResources(), R.drawable.greenscreen);
        screen = Bitmap.createScaledBitmap(screen, width, height, true);

        Random r1 = new Random();
        int xx = r1.nextInt(width);
        answerBalloon = new AnswerBalloon(xx, 0, 5);

        setBackgroundColor(Color.BLUE);
        myHandler.sendEmptyMessageDelayed(0, 1000);
        makeQuestion();
    }

    @Override
    synchronized public void onDraw(Canvas canvas){

        moveBalloon();
        checkCollision();

        if(balloonArrayList.size() < 5){
            Random r2 = new Random();
            int x = r2.nextInt(width - buttonWidth);
            int y = r2.nextInt(height / 4);
            balloonArrayList.add(new Balloon(x, -y, 5));
        }

        Paint p1 = new Paint();
        p1.setColor(Color.WHITE);
        p1.setTextSize(width / 14);

        canvas.drawBitmap(screen, 0, 0, p1);
        canvas.drawText("Score : " + Integer.toString(score), 0, height / 12, p1);
        canvas.drawText("Question : " + Integer.toString(num1) + " + " + Integer.toString(num2), 0, height*2 / 12, p1);

        canvas.drawBitmap(basketImg, basket.x, basket.y, p1);
        canvas.drawBitmap(leftKey, leftKey_x, leftKey_y, p1);
        canvas.drawBitmap(rightKey, rightKey_x, rightKey_y, p1);

        for(Balloon tmp : balloonArrayList){
            canvas.drawBitmap(balloonImg, tmp.x, tmp.y, p1);
        }
        for(int i = 0; i < balloonArrayList.size(); i++){
            canvas.drawText(Integer.toString(worngNumber[i]), balloonArrayList.get(i).x + balloonWidth / 6, balloonArrayList.get(i).y + balloonHeight * 5 / 6, p1);
        }

        canvas.drawBitmap(balloonImg, answerBalloon.x, answerBalloon.y, p1);
        canvas.drawText(Integer.toString(ans), answerBalloon.x + balloonWidth / 6, answerBalloon.y + balloonHeight * 2 / 3, p1);

        if(answerBalloon.y > height) answerBalloon.y = -50;

        count++;
    }

    public void makeQuestion(){
        Random r3 = new Random();

        num1 = r3.nextInt(99) + 1;
        num2 = r3.nextInt(99) + 1;
        ans = num1 + num2;

        for(int i = 0; i < 5; i++){
            int tmp = r3.nextInt(197) + 1;
            while(tmp == ans){
                tmp = r3.nextInt(197) + 1;
            }

            worngNumber[i] = tmp;
        }
    }

    public void moveBalloon(){
        for(int i = 0; i < balloonArrayList.size(); i++){
            balloonArrayList.get(i).move();

            if(balloonArrayList.get(i).y > height) balloonArrayList.get(i).y = -100;
        }

        answerBalloon.move();
    }

    public void checkCollision(){
        for(int i = 0; i < balloonArrayList.size(); i++){
            int balX = balloonArrayList.get(i).x;
            int balY = balloonArrayList.get(i).y;

            if(balX + balloonWidth / 2 >= basket.x && balX + balloonWidth / 2 <= basket.x + basketWidth && balY + balloonHeight >= basket.y){
                balloonArrayList.remove(i);
                score -= 10;
            }
        }

        int ansX = answerBalloon.x;
        int ansY = answerBalloon.y;
        if(ansX + balloonWidth / 2 >= basket.x && ansX + balloonWidth / 2 <= basket.x + basketWidth && ansY + balloonHeight >= basket.y){
            score += 30;
            makeQuestion();

            Random r1 = new Random();
            int xx = r1.nextInt(width - buttonWidth);
            answerBalloon.x = xx;
            answerBalloon.y = -50;
        }
    }

    Handler myHandler = new Handler(){

        public void handleMessage(Message message){
            invalidate();
            myHandler.sendEmptyMessageDelayed(0, 30);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x = 0, y = 0;

        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
            x = (int) event.getX();
            y = (int) event.getY();
        }

        if((x > leftKey_x) && (x < leftKey_x + buttonWidth) && (y > leftKey_y) && (y < leftKey_y + buttonWidth))
            basket.moveLeft();
        if((x > rightKey_x) && (x < rightKey_x + buttonWidth) && (y > rightKey_y) && (y < rightKey_y + buttonWidth))
            basket.moveRight();

        return true;
    }
}
