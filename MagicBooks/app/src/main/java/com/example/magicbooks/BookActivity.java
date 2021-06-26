package com.example.magicbooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class BookActivity extends AppCompatActivity {

    String answer[];
    TextView textView;
    Random random;

    @Override
    public void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.book_content);

        setDefault(getIntent().getIntExtra("sort", 0));
    }

    private void setDefault(int tmp) {

        switch (tmp) {
            case 1:
                answer = getResources().getStringArray(R.array.normal);
                break;
            case 2:
                answer = getResources().getStringArray(R.array.concern);
                break;
            case 3:
                answer = getResources().getStringArray(R.array.lover);
                break;
        }

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        textView = findViewById(R.id.content_text);
        String string = "질문을 생각하면서 아래에 손을 올려주세요.\n";
        string += "기다리던 답이 보일 겁니다.";
        textView.setText(string);

        ImageView imageView = (ImageView) findViewById(R.id.content_image);
        imageView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        int idx = random.nextInt(answer.length);
                        textView.setText(answer[idx]);
                        return false;
                }

                return false;
            }
        });
    }
}
