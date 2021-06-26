package com.example.balloonmathgame;

public class Basket {
    public int x, y;
    public int speed;

    Basket(int x, int y, int speed){
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void moveRight(){
        x += speed;
    }
    public void moveLeft(){
        x -= speed;
    }
}
