package com.example.findtresure;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpScreen extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_screen);

        setText();
    }

    public void return_main(View view){finish();}
    private void setText(){
         textView = (TextView) findViewById(R.id.help_text);
        String string = "1인용)\n맵에 자동으로 보물상자가 숨겨집니다.\n";
        string += "보물상자를 최대한 빨리 찾아주세요 !! \n";

        string += "\n\n\n\n\n2인용)\n한 사람이 보물상자를 맵에 숨깁니다.\n";
        string += "다른 사람은, 숨긴 보물상자를 최대한 빨리 찾아내면 됩니다 !!";

        string += "\n\n\n만든이 : 김동하\n오류 문의 : kdh101800@gmail.com";
        textView.setText(string);
    }
}
