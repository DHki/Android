package com.example.balloonmathgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int navigationHeight = 0;
        int resId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if(resId > 0) navigationHeight = getResources().getDimensionPixelSize(resId);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels - navigationHeight;

        setContentView(new MyView(this, width, height));
    }
}

